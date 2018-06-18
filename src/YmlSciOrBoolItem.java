import java.io.FileOutputStream;

/**
 * Created by EvanChoo on 5/25/18.
 */
public class YmlSciOrBoolItem extends YmlItem
{
    private String value;

    public YmlSciOrBoolItem(String name, boolean isInAnArray, String value)
    {
        super(name, isInAnArray);
        this.value = value;
    }

    public YmlSciOrBoolItem(String name, boolean isInAnArray)
    {
        super(name, isInAnArray);
        this.value = null;
    }

    @Override
    public void print(int indentation, FileOutputStream fop) throws Exception
    {
        if (this.getValue().charAt(0) == 'f' || this.getValue().charAt(0) == 't')
        {
            for(int i=0; i<indentation; i++)
                //System.out.print(" ");
                fop.write(" ".getBytes());
            String s = "\"" + this.getName() + "\": " + getValue();
            fop.write(s.getBytes());
            //System.out.print("\"" + this.getName() + "\": " + getValue());
        }
        else
        {
            for(int i=0; i<indentation; i++)
                //System.out.print(" ");
                fop.write(" ".getBytes());
            String s = "\""+this.getName()+"\": "+getValue().replaceAll("e", "E");
            fop.write(s.getBytes());
            //System.out.print("\""+this.getName()+"\": "+getValue().replaceAll("e", "E"));
        }
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
}
