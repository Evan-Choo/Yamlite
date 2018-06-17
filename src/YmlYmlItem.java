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
    public void print()
    {

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
