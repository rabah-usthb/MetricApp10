package application.Testing;

//test class
public class Test extends
  parent {
    public int publicVariable; //public variable     
    private String privateVariable;
    protected boolean protectedVariable;
    int packagePrivateVariable;

  /*
  	this is a 
    multi-line 
    comments
   
   */
  
  
    public Test() {
        super();
    }

    public void publicMethod(int a) throws
     Exception {
       int b = a-2;
       return b/a;
    }

    private void privateMethod() {
        System.out.println("Private Method");
    }

    /* hello world */
}
