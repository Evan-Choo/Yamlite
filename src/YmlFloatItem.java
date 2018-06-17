/**
 * Created by EvanChoo on 5/25/18.
 */
public class YmlFloatItem extends YmlItem
{
    private float value;

    public YmlFloatItem(String name, boolean isInAnArray, float value)
    {
        super(name, isInAnArray);
        this.value = value;
    }

    @Override
    public void print()
    {
        System.out.print("\""+this.getName()+"\": "+getValue());
    }

    public String getValue()
    {
        return Float.toString(value);
    }

    public void setValue(float value)
    {

        this.value = value;
    }
}
