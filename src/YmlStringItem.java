/**
 * Created by EvanChoo on 5/25/18.
 */
public class YmlStringItem extends YmlItem
{
    private String value;

    public YmlStringItem(String name, boolean isInAnArray, String value)
    {
        super(name, isInAnArray);
        this.value = value;
    }

    public YmlStringItem(String name, boolean isInAnArray)
    {
        super(name, isInAnArray);
        this.value = null;
    }

    @Override
    public void print()
    {
        System.out.print("\""+this.getName()+"\": "+getValue());
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
