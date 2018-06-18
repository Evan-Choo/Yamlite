import java.io.FileOutputStream;
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
    public void print(int indentation, FileOutputStream fop) throws Exception
    {
        /**
         *  isInArray==true&&has name -> {name: [...]}
         *
         *  isInArray==true&&has no name -> [...]
         *
         *  isInArray==false -> name:[...]
         */
        if (this.isInAnArray()==true&&this.getName() != null)
        {
            for(int i=0; i<indentation; i++)
                fop.write(" ".getBytes());
            fop.write("{".getBytes());
            fop.write(System.lineSeparator().getBytes());
            for(int i=0; i<indentation+2; i++)
                fop.write(" ".getBytes());
            String s = "\"" + this.getName() + "\": [";
            //System.out.println("\"" + this.getName() + "\": [");
            fop.write(s.getBytes());
            fop.write(System.lineSeparator().getBytes());
        }
        else if(this.isInAnArray()==true&&this.getName()==null)
        {
            for(int i=0; i<indentation; i++)
                //System.out.print(" ");
                fop.write(" ".getBytes());
            //System.out.println("[");
            fop.write("[".getBytes());
            fop.write(System.lineSeparator().getBytes());
        }
        else if(this.isInAnArray()==false&&this.getName()!=null)
        {
            for(int i=0; i<indentation; i++)
                //System.out.print(" ");
                fop.write(" ".getBytes());
            String s = "\"" + this.getName() + "\": [";
            //System.out.println("\"" + this.getName() + "\": [");
            fop.write(s.getBytes());
            fop.write(System.lineSeparator().getBytes());
        }
        else if(this.isInAnArray()==false&&this.getName()==null)
        {
            //System.out.println("[");
            fop.write("[".getBytes());
            fop.write(System.lineSeparator().getBytes());

            for(int i=0; i<value.size(); i++)
            {
                value.get(i).print(indentation+2, fop);
                if (i != value.size() - 1)
                {
                    //System.out.print(",");
                    fop.write(",".getBytes());
                }
                //System.out.println();
                fop.write(System.lineSeparator().getBytes());
            }

            //System.out.println("]");
            fop.write("]".getBytes());
            fop.write(System.lineSeparator().getBytes());

            return;
        }

        for(int i=0; i<value.size(); i++)
        {
            value.get(i).print(indentation+2, fop);
            if (i != value.size() - 1)
            {
                //System.out.print(",");
                fop.write(",".getBytes());
            }
            //System.out.println();
            fop.write(System.lineSeparator().getBytes());
        }

        for(int i=0; i<indentation; i++)
            //System.out.print(" ");
            fop.write(" ".getBytes());
        //System.out.print("]");
        fop.write("]".getBytes());

        if (this.isInAnArray() == true && this.getName()!=null)
        {
            //System.out.print("}");
            fop.write("}".getBytes());
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

    @Override
    public ArrayList<YmlItem> getRealValue()
    {
        return value;
    }
}
