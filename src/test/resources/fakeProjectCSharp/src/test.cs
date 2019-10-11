using System;

[Obsolete]
class BasicAttributeDemo
{

    [Obsolete]
    private string name;

    [method: Obsolete]
    public void MyFirstdeprecatedMethod([Obsolete] string a)
    {
        Console.WriteLine("Called MyFirstdeprecatedMethod().");
    }

    [ObsoleteAttribute(SetLastError=false, ExactSpelling=false)]
    public void MySecondDeprecatedMethod()
    {
        Console.WriteLine("Called MySecondDeprecatedMethod().");
    }

    [Obsolete("You shouldn't use this method anymore.")]
    public void MyThirdDeprecatedMethod()
    {
        Console.WriteLine("Called MyThirdDeprecatedMethod().");
    }

    // make the program thread safe for COM
    [STAThread("user32.dll", ExactSpelling=false, SetLastError=false)]
    static void Main(string[] args)
    {
        BasicAttributeDemo attrDemo = new BasicAttributeDemo();

        attrDemo.MyFirstdeprecatedMethod();
        attrDemo.MySecondDeprecatedMethod();
        attrDemo.MyThirdDeprecatedMethod();
    }
}