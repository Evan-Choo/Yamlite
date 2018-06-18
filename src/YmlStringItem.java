import java.awt.peer.FontPeer;
import java.io.FileOutputStream;

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
    public void print(int indentation, FileOutputStream fop) throws Exception
    {
        if (this.isInAnArray() && this.getName() != null)
        {
            for(int i=0; i<indentation; i++)
                //System.out.print(" ");
                fop.write(" ".getBytes());
            //System.out.println("{");
            fop.write("{".getBytes());
            fop.write(System.lineSeparator().getBytes());

            for(int i=0; i<indentation+2; i++)
                //System.out.print(" ");
                fop.write(" ".getBytes());
            String s = "\""+this.getName()+"\": "+getValue();
            fop.write(s.getBytes());
            //System.out.println("\""+this.getName()+"\": "+getValue());
            for(int i=0; i<indentation; i++)
                //System.out.print(" ");
                fop.write(" ".getBytes());
            //System.out.print("}");
            fop.write("}".getBytes());
        }
        else
        {
            for(int i=0; i<indentation; i++)
                //System.out.print(" ");
                fop.write(" ".getBytes());
            String s = "\"" + this.getName() + "\": " + getValue();
            fop.write(s.getBytes());
            //System.out.print("\"" + this.getName() + "\": " + getValue());
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
