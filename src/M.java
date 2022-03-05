public class M {
    public static void main(String[] args) {
        int i = 0;
        int[] nArray = new int[5];

        while(true)
        {
            try
            {
                nArray[i] = i;
            }
            catch(Exception ex)
            {
                System.out.println("\n" + ex.toString());
                break;
            }

            System.out.print(i);
            i++;
        }
    }
}
