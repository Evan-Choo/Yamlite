import java.io.FileOutputStream;

/**
 * Created by EvanChoo on 5/25/18.
 */
public class YmlIntItem extends YmlItem
{
    private int value;

    public YmlIntItem(String name, boolean isInAnArray, int value)
    {
        super(name, isInAnArray);
        this.value = value;
    }

    public YmlIntItem(String name, boolean isInAnArray)
    {
        super(name, isInAnArray);
        this.value = 0;
    }

    @Override
    public void print(int indentation, FileOutputStream fop) throws Exception
    {
        if(this.getName()!=null)
        {
            for(int i=0; i<indentation; i++)
                fop.write(" ".getBytes());
            String s = "\"" + this.getName() + "\": " + getValue();
            fop.write(s.getBytes());
        }
        else
        {
            for(int i=0; i<indentation; i++)
                fop.write(" ".getBytes());
            fop.write(getValue().getBytes());
        }

    }

    public String getValue()
    {
        return Integer.toString(value);
    }

    public void setValue(int value)
    {
        this.value = value;
    }
}
