import java.io.FileOutputStream;

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
    public void print(int indentation, FileOutputStream fop) throws Exception
    {
        if(this.getName()!=null)
        {
            for(int i=0; i<indentation; i++)
                //System.out.print(" ");
                fop.write(" ".getBytes());
            String s = "\""+this.getName()+"\": "+getValue();
            fop.write(s.getBytes());
            //System.out.print("\""+this.getName()+"\": "+getValue());
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
        return Float.toString(value);
    }

    public void setValue(float value)
    {

        this.value = value;
    }
}
