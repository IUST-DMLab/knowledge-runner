package ir.ac.iust.dml.kg.knowledge.runner.logic;


import ir.ac.iust.dml.kg.knowledge.runner.access.entities.Job;
import ir.ac.iust.dml.kg.knowledge.runner.access.entities.JobState;
import ir.ac.iust.dml.kg.knowledge.runner.access.entities.JobStep;
import ir.ac.iust.dml.kg.knowledge.runner.access.entities.RunHistory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Map;

public class Runner extends Thread {
    private final String PROGRESS_START = "#progress";
    private final Job job;
    private final RunnerListener listener;
    private boolean running;
    private Process current;

    public Runner(Job job, RunnerListener listener) {
        this.job = job;
        this.listener = listener;
        this.running = true;
    }

    @Override
    public void run() {
        try (RunHistory hist = listener.open(job)) {
            try {
                listener.started(job);
                for (JobStep step : job.getSteps())
                    if (running) {
                        final ProcessBuilder builder = new ProcessBuilder();
                        builder.command(step.commands());
                        if (step.getEnvironment() != null)
                            for (Map.Entry<String, String> entry : step.getEnvironment().entrySet())
                                step.getEnvironment().put(entry.getKey(), entry.getValue());
                        if (step.getWorkingDirectory() != null)
                            builder.directory(Paths.get(step.getWorkingDirectory()).toFile());
                        builder.redirectErrorStream(true);
                        current = builder.start();
                        final Thread thr1 = readOutput(current, hist);
                        final Thread thr2 = readError(current, hist);
                        thr1.join();
                        thr2.join();
                        if (current.exitValue() != 0) {
                            listener.reportStep(job, step, JobState.Failed);
                            if (!step.isContinueOnFail()) {
                                listener.completed(job, JobState.Failed, null);
                                return;
                            }
                        } else
                            listener.reportStep(job, step, JobState.Succeed);
                    }
                listener.completed(job, JobState.Succeed, null);
            } catch (Exception ex) {
                listener.completed(job, JobState.Failed, ex);
            }
        } catch (Exception ex) {
            listener.completed(job, JobState.HistoryUnavailable, ex);
        }
    }

    private Thread readOutput(Process process, RunHistory history) {
        final Thread thr = new Thread(() -> {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith(PROGRESS_START))
                        try {
                            final String str = line.substring(PROGRESS_START.length()).trim();
                            final float progress = Float.parseFloat(str);
                            listener.reportProgress(job, progress);
                        } catch (Throwable ignored) {

                        }
                    history.appendOutput(line);
                }
            } catch (Exception ignored) {
            }
        });
        thr.start();
        return thr;
    }

    private Thread readError(Process process, RunHistory history) {
        final Thread thr = new Thread(() -> {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = in.readLine()) != null) {
                    history.appendError(line);
                }
            } catch (Exception ignored) {
            }
        });
        thr.start();
        return thr;
    }

    void shutdown() {
        running = false;
        current.destroy();
    }
}
