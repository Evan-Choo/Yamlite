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
    public void print()
    {
        if(this.getName()!=null)
        {
            System.out.print("\"" + this.getName() + "\": " + getValue());
        }
        else
        {
            System.out.print(getValue());
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
