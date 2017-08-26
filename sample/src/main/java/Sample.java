public class Sample {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            System.out.println("#progress " + i);
            Thread.sleep(500);
        }
    }
}
