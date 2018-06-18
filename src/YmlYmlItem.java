import java.io.FileOutputStream;

/**
 * Created by EvanChoo on 6/15/18.
 */
public class YmlYmlItem extends YmlItem
{
    private YmlItem value;

    public YmlYmlItem(String name, boolean isInAnArray, YmlItem value)
    {
        super(name, isInAnArray);
        this.value = value;
    }

    public YmlYmlItem(String name, boolean isInAnArray)
    {
        super(name, isInAnArray);
        this.value = null;
    }

    @Override
    public void print(int indentation, FileOutputStream fop) throws Exception
    {
        if(this.getName()!=null)
        {
            for(int i=0; i<indentation; i++)
                fop.write(" ".getBytes());
            fop.write(this.getName().getBytes());
            fop.write(":".getBytes());
            fop.write(System.lineSeparator().getBytes());
        }
        for(int i=0; i<indentation; i++)
            fop.write(" ".getBytes());
        fop.write("{".getBytes());
        fop.write(System.lineSeparator().getBytes());
        value.print(indentation+2, fop);
        fop.write("}".getBytes());
        //fop.write(System.lineSeparator().getBytes());
    }

    @Override
    public String getValue()
    {
        return null;
    }

    @Override
    public void addItem(YmlItem item)
    {
        this.value = item;
    }
}
