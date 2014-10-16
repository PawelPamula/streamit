package streamit;

/**
 * Hello world!
 *
 */
public class App 
{
    public static int f(int n) throws IllegalArgumentException
    {
        if(n < 0) throw new IllegalArgumentException("n < 0");
        if(n == 0 || n == 1) return 1;
        return n*f(n-1);
    }

    public static void main(String[] args)
    {
        System.out.println("Hello World!");
        System.out.println(f(5));
    }

}
