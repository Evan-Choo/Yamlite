import java.util.ArrayList;

/**
 * Created by EvanChoo on 5/25/18.
 */
public class YmlArrayItem extends YmlItem
{
    private ArrayList<YmlItem> value;

    public YmlArrayItem(String name, boolean isInAnArray, ArrayList<YmlItem> value)
    {
        super(name, isInAnArray);
        this.value = value;
    }

    public YmlArrayItem(String name, boolean isInAnArray)
    {
        super(name, isInAnArray);
        this.value = new ArrayList<>();
    }


    @Override
    public void print()
    {
        if (this.isInAnArray() == true)
        {
            System.out.println("{");
        }

        if (this.getName() != null)
        {
            System.out.println("{");
            System.out.println("\"" + this.getName() + "\": [");
        }
        else
        {
            System.out.println("[");
        }

        for(int i=0; i<value.size(); i++)
        {
            value.get(i).print();
            if (i != value.size() - 1)
            {
                System.out.print(",");
            }
            System.out.println();
        }

        System.out.println("]");

        if (this.isInAnArray() == true)
        {
            System.out.println("}");
        }
    }

    public String getValue()
    {
        return null;
    }

    public void setValue(ArrayList<YmlItem> value)
    {
        this.value = value;
    }

    @Override
    public void addItem(YmlItem item)
    {
        value.add(item);
    }
}
