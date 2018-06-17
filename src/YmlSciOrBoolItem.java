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
    public void print()
    {
        if (this.getValue().charAt(0) == 'f' || this.getValue().charAt(0) == 't')
        {
            System.out.print("\"" + this.getName() + "\": " + getValue());
        }
        else
        {
            System.out.print("\""+this.getName()+"\": "+getValue().replaceAll("e", "E"));
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
