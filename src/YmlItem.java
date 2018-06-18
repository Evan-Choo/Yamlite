import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by EvanChoo on 5/25/18.
 */
public abstract class YmlItem
{
    private String name;
    /*if item is in an array, then it'll be printed surrounded by curly braces*/
    private boolean isInAnArray;

    public YmlItem(String name, boolean isInAnArray)
    {
        this.name = name;
        this.isInAnArray = isInAnArray;
    }

    public abstract void print(int indentation, FileOutputStream fop) throws Exception;

    public abstract String getValue();

    public void addItem(YmlItem item) throws Exception
    {
        throw new Exception("can't add item");
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isInAnArray()
    {
        return isInAnArray;
    }

    public void setInAnArray(boolean inAnArray)
    {
        isInAnArray = inAnArray;
    }

    public ArrayList<YmlItem> getRealValue()
    {
        return null;
    }
}
