import jdk.nashorn.internal.runtime.regexp.joni.encoding.CharacterType;

import javax.sound.sampled.Line;
import java.io.BufferedReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by EvanChoo on 5/24/18.
 */

public class YmlProcess
{
    //private static int lineNum = 0;
    private static int columnNum = 0;
    private static LineNumberReader lineNumberReader;
    /* use to determine whether the line is the first non-comment line;
     *
     * if the first non-comment line begin with "- ", then the outermost of the yml file is array
     *
     * else the outermost is key-value pair
     */
    private static boolean firstNoncommentLine = false;

    public static String isValid(String filepath) throws Exception
    {

        //BufferedReader br = FileUtil.findFile(filepath);
        lineNumberReader = FileUtil.lineNumberReader(filepath);

        String line;

        while((line = lineNumberReader.readLine()) != null)
        {

            //lineNum = lineNumberReader.getLineNumber();

            if(line.length()!=0)
            {
                if(!processLine(line))
                {
                    if(JsonFile.getInstance().isOutermostArray())
                    {
                        processOutermostArray(lineNumberReader, line);
                    }
                    else
                    {
                        processArrayOrKeyValuePair(lineNumberReader, line);
                    }
                }
            }

            columnNum = 0;
        }
        //JsonFile.getInstance().generateJsonFile();

        return "Valid";
    }

    /**
     *
     * @param line
     * @return
     *
     * 1. true: the outermost of the line is a key-value pair
     *
     * 2. false: the outermost of the line is an array
     *
     */
    private static boolean processLine(String line) throws Exception
    {
        String name = getKey(line, columnNum);

        //outermost is array
        if(name==null)
        {
            return false;
        }

        //the line is comment line
        if(name.length()==0)
            return true;

        //the value is array or key-value pair
        if(line.length()==columnNum+1)
        {
            return false;
        }

        columnNum += 2;

        YmlItem item = getValue(line, name, columnNum, line.length()-1);
        JsonFile.getInstance().addItem(item);
        
        return true;
    }

    /**
     *
     * @param line
     * @return the identifier/key of the key-value pair
     */
    private static String getKey(String line, int beginIndex) throws Exception
    {
        int state = 0;
        boolean isFinished = false;
        char[] chars = line.toCharArray();

        while(!isFinished)
        {
            switch (state)
            {
                case 0:
                    if (Validator.getInstance().isaToz(chars[columnNum]) || Validator.getInstance().isAToZ(chars[columnNum]))
                    {
                        //the outermost of the file is key-value pair
                        if (!firstNoncommentLine)
                        {
                            JsonFile.getInstance().setIsOutermostArray(false);
                            firstNoncommentLine = true;
                        }
                        state = 1;
                        columnNum++;
                    }
                    else if (chars[columnNum] == '-')
                    {
                        if (!firstNoncommentLine)
                        {
                            JsonFile.getInstance().setIsOutermostArray(true);
                            firstNoncommentLine = true;

                            return null;
                        }

                        throw new Exception(getCurPos()+", the outermost can't be both array and key-value pair");
                    }
                    else if (chars[columnNum] == '#')
                    {
                        isFinished = true;
                    }
                    else
                    {
                        throw new Exception(getCurPos()+ ", identifier can't begin with " + chars[columnNum]);
                    }
                    break;

                case 1:
                    if (Validator.getInstance().isaToz(chars[columnNum]) || Validator.getInstance().isAToZ(chars[columnNum]) || Validator.getInstance().is0To9(chars[columnNum]))
                    {
                        state = 1;
                        columnNum++;
                    }
                    else if (chars[columnNum] == '_')
                    {
                        state = 2;
                        columnNum++;
                    }
                    else if (chars[columnNum] == ':')
                    {
                        isFinished = true;
                    }
                    else
                    {
                        throw new Exception(getCurPos()+ ", illegal identifier character");
                    }
                    break;

                case 2:
                    if (Validator.getInstance().isaToz(chars[columnNum]) || Validator.getInstance().isAToZ(chars[columnNum]) || Validator.getInstance().is0To9(chars[columnNum]))
                    {
                        state = 1;
                        columnNum++;
                    }
                    else if (chars[columnNum] == '_')
                    {
                        state = 2;
                        columnNum++;
                    }
                    else
                    {
                        throw new Exception(getCurPos()+ ", illegal identifier character");
                    }
                    break;
            }
        }

        return line.substring(beginIndex, columnNum);
    }

    private static YmlItem getValue(String line, String name, int beginIndex, int endIndex) throws Exception
    {
        int state = 0;
        boolean isFinished = false;
        char[] chars = line.toCharArray();
        YmlItem item = null;

        while(!isFinished)
        {
            if(columnNum > endIndex)
                throw new Exception(getCurPos()+" illegal expression");

            switch (state)
            {
                case 0:
                    if(Validator.getInstance().is1To9(chars[columnNum]))
                    {
                        if(columnNum==endIndex)
                        {
                            item = new YmlIntItem(name, false, Integer.parseInt(String.valueOf(chars[columnNum])));
                            isFinished = true;
                        }
                        else
                        {
                            state = 1;
                            columnNum++;
                        }
                    }
                    else if(chars[columnNum]=='\"')
                    {
                        state = 10;
                        columnNum++;
                    }
                    else if(chars[columnNum]=='t')
                    {
                        state = 13;
                        columnNum++;
                    }
                    else if(chars[columnNum]=='f')
                    {
                        state = 17;
                        columnNum++;
                    }
                    else
                    {
                        throw new Exception(getCurPos()+ ", expected <1-9, \", f, r>");
                    }
                    break;

                case 1:
                    if(Validator.getInstance().is0To9(chars[columnNum])){
                        if(columnNum==endIndex)
                        {
                            item = new YmlIntItem(name, false, Integer.parseInt(line.substring(beginIndex, columnNum+1)));
                            isFinished = true;
                        }
                        else
                        {
                            state = 1;
                            columnNum++;
                        }
                    }
                    else if(chars[columnNum]=='#')
                    {
                        item = new YmlIntItem(name, false, Integer.parseInt(line.substring(beginIndex, columnNum)));
                        isFinished = true;
                    }
                    else if(chars[columnNum]=='.')
                    {
                        state = 3;
                        columnNum++;
                    }
                    else if(chars[columnNum]=='E' || chars[columnNum]=='e')
                    {
                        item = new YmlSciOrBoolItem(name, false);
                        state = 6;
                        columnNum++;
                    }
                    else if(chars[columnNum]==' ')
                    {
                        if(columnNum==endIndex)
                        {
                            item = new YmlIntItem(name, false, Integer.parseInt(line.substring(beginIndex, columnNum)));
                            isFinished = true;
                        }
                        else
                        {
                            state = 22;
                            columnNum++;
                        }
                    }
                    else
                    {
                        throw new Exception(getCurPos()+" illgeal value");
                    }
                    break;

                case 3:
                    if(Validator.getInstance().is0To9(chars[columnNum]))
                    {
                        state = 4;
                        columnNum++;
                    }
                    else
                    {
                        throw new Exception(getCurPos()+" illgal value");
                    }
                    break;

                case 4:
                    if(Validator.getInstance().is0To9(chars[columnNum]))
                    {
                        if(columnNum == endIndex)
                        {
                            item = new YmlFloatItem(name, false, Float.parseFloat(line.substring(beginIndex)));
                            isFinished = true;
                        }
                        else
                        {
                            state = 4;
                            columnNum++;
                        }
                    }
                    else if(chars[columnNum]=='E' || chars[columnNum]=='e')
                    {
                        state = 6;
                        columnNum++;
                    }
                    else if(chars[columnNum]=='#')
                    {
                        item = new YmlFloatItem(name, false, Float.parseFloat(line.substring(beginIndex, columnNum)));
                        isFinished = true;
                    }
                    else if(chars[columnNum]==' ')
                    {
                        if(columnNum==endIndex)
                        {
                            item = new YmlFloatItem(name, false, Float.parseFloat(line.substring(beginIndex, columnNum)));
                            isFinished = true;
                        }
                        else
                        {
                            state = 24;
                            columnNum++;
                        }
                    }
                    else
                    {
                        throw new Exception(getCurPos()+" illgal expression");
                    }
                    break;

                case 6:
                    if(chars[columnNum]=='-' || chars[columnNum]=='+')
                    {
                        state = 7;
                        columnNum++;
                    }
                    else if(Validator.getInstance().is1To9(chars[columnNum]))
                    {
                        state = 8;
                        columnNum++;
                    }
                    else
                    {
                        throw new Exception(getCurPos()+" illegal expression");
                    }
                    break;

                case 7:
                    if(Validator.getInstance().is1To9(chars[columnNum]))
                    {
                        state = 8;
                        columnNum++;
                    }
                    else
                    {
                        throw new Exception(getCurPos()+" illegal expression");
                    }
                    break;

                case 8:
                    if(Validator.getInstance().is0To9(chars[columnNum]))
                    {
                        if(columnNum==endIndex)
                        {
                            item = new YmlSciOrBoolItem(name, false, line.substring(beginIndex, columnNum+1));
                            isFinished = true;
                        }
                        else
                        {
                            state = 8;
                            columnNum++;
                        }
                    }
                    else if(chars[columnNum]=='#')
                    {
                        item = new YmlSciOrBoolItem(name, false, line.substring(beginIndex, columnNum));
                        isFinished = true;
                    }
                    else if(chars[columnNum]==' ')
                    {
                        if(columnNum==endIndex)
                        {
                            item = new YmlSciOrBoolItem(name, false, line.substring(beginIndex, columnNum));
                            isFinished = true;
                        }
                        else
                        {
                            state = 27;
                            columnNum++;
                        }
                    }
                    else
                    {
                        throw new Exception(getCurPos()+" illegal expression");
                    }
                    break;

                case 22:
                    if(chars[columnNum]==' ')
                    {
                        if(columnNum==endIndex)
                        {
                            item = new YmlIntItem(name, false, Integer.parseInt(line.substring(beginIndex).trim()));
                            isFinished = true;
                        }
                        else
                        {
                            state = 22;
                            columnNum++;
                        }
                    }
                    else if(chars[columnNum]=='#')
                    {
                        item = new YmlIntItem(name, false, Integer.parseInt(line.substring(beginIndex).trim()));
                        isFinished = true;
                    }
                    else
                    {
                        throw new Exception(getCurPos()+" illegal expression");
                    }
                    break;

                case 24:
                    if(chars[columnNum]==' ')
                    {
                        if(columnNum==endIndex)
                        {
                            item = new YmlFloatItem(name, false, Float.parseFloat(line.substring(beginIndex).trim()));
                            isFinished = true;
                        }
                        else
                        {
                            state = 24;
                            columnNum++;
                        }
                    }
                    else if(chars[columnNum]=='#')
                    {
                        item = new YmlFloatItem(name, false, Float.parseFloat(line.substring(beginIndex).trim()));
                        isFinished = true;
                    }
                    else
                    {
                        throw new Exception(getCurPos()+" illegal expression");
                    }
                    break;

                case 27:
                    if(chars[columnNum]==' ')
                    {
                        if(columnNum==endIndex)
                        {
                            item = new YmlSciOrBoolItem(name, false, line.substring(beginIndex).trim());
                            isFinished = true;
                        }
                        else
                        {
                            state = 27;
                            columnNum++;
                        }
                    }
                    else if(chars[columnNum]=='#')
                    {
                        item = new YmlSciOrBoolItem(name, false, line.substring(beginIndex).trim());
                        isFinished = true;
                    }
                    else
                    {
                        throw new Exception(getCurPos()+" illegal expression");
                    }
                    break;

                case 10:
                    if(Validator.getInstance().isAscii(chars[columnNum]) && chars[columnNum] != '\"')
                    {
                        state = 10;
                        columnNum++;
                    }
                    else if(chars[columnNum]=='\"')
                    {
                        item = new YmlStringItem(name, false, line.substring(beginIndex, columnNum+1));
                        isFinished = true;
                    }
                    else
                    {
                        throw new Exception(getCurPos()+" illegal expression");
                    }
                    break;

                case 13:
                    if(chars[columnNum]=='r')
                    {
                        state = 14;
                        columnNum++;
                    }
                    else
                    {
                        throw new Exception(getCurPos()+" illegal expression");
                    }
                    break;

                case 14:
                    if(chars[columnNum]=='u')
                    {
                        state = 15;
                        columnNum++;
                    }
                    else
                    {
                        throw new Exception(getCurPos()+" illegal expression");
                    }
                    break;

                case 15:
                    if(chars[columnNum]=='e')
                    {
                        item = new YmlSciOrBoolItem(name, false, line.substring(beginIndex, columnNum+1));
                        isFinished = true;
                    }
                    else
                    {
                        throw new Exception(getCurPos()+" illegal expression");
                    }
                    break;

                case 17:
                    if(chars[columnNum]=='a')
                    {
                        state = 18;
                        columnNum++;
                    }
                    else
                    {
                        throw new Exception(getCurPos()+" illegal expression");
                    }
                    break;

                case 18:
                    if(chars[columnNum]=='l')
                    {
                        state = 19;
                        columnNum++;
                    }
                    else
                    {
                        throw new Exception(getCurPos()+" illegal expression");
                    }
                    break;

                case 19:
                    if(chars[columnNum]=='s')
                    {
                        state = 20;
                        columnNum++;
                    }
                    else
                    {
                        throw new Exception(getCurPos()+" illegal expression");
                    }
                    break;

                case 20:
                    if(chars[columnNum]=='e')
                    {
                        item = new YmlSciOrBoolItem(name, false, line.substring(beginIndex, columnNum));
                        isFinished = true;
                    }
                    else
                    {
                        throw new Exception(getCurPos()+" illegal expression");
                    }
                    break;
            }
        }

        return item;
    }
    
    private static String getCurPos()
    {
        return "line " + lineNumberReader.getLineNumber() + ", position " + (columnNum + 1);
    }

    private static void processOutermostArray(LineNumberReader lineNumberReader, String line) throws Exception
    {
        ArrayList<String> content = new ArrayList<>();
        content.add(line);

        line = lineNumberReader.readLine();
        while (line != null)
        {
            //end of the array
            if (line.length() != 0 && !isComment(line)) content.add(line);
            line = lineNumberReader.readLine();
        }

        ArrayList<ArrayLine> arrayLines = produceArrayLines(content);

        YmlArrayItem arrayItem = new YmlArrayItem(null, false);

        produceArrayItem(arrayLines, arrayItem);
    }

    private static void processArrayOrKeyValuePair(LineNumberReader lineNumberReader, String line) throws Exception
    {
        String name = line.substring(0, columnNum);

        line = lineNumberReader.readLine();
        while (line.length() == 0 || isComment(line)) line = lineNumberReader.readLine();

        processArrayAndKeyValue(name, lineNumberReader, line);
    }

    private static void processArrayAndKeyValue(String name, LineNumberReader lineNumberReader, String line) throws Exception
    {
        ArrayList<String> content = new ArrayList<>();
        content.add(line);

        line = lineNumberReader.readLine();
        //boolean isFinished = false;
        while(line!=null)
        {
            //end of the array
            if(!isComment(line) && line.charAt(0)!=' ')
            {
                //isFinished = true;
                break;
            }
            else if(!isComment(line)&&line.length()!=0)
                content.add(line);

            line = lineNumberReader.readLine();
        }

        ArrayList<ArrayLine> arrayLines = produceArrayLines(content);

        YmlArrayItem arrayItem = new YmlArrayItem(name, false);

        produceArrayItem(arrayLines, arrayItem);
    }

    private static boolean isComment(String line) throws Exception
    {
        boolean isFinished = false;
        char[] chars = line.toCharArray();
        int state = 0;
        columnNum = 0;

        if(line.length()==0)
            return true;

        while(!isFinished)
        {
            switch (state)
            {
                case 0:
                    if(chars[columnNum]=='#')
                        return true;
                    else if(chars[columnNum]==' ')
                    {
                        state = 1;
                        columnNum++;
                    }
                    else
                    {
                        return false;
                    }
                    break;

                case 1:
                    if(chars[columnNum]==' ')
                    {
                        state = 0;
                        columnNum++;
                    }
                    else
                    {
                        throw new Exception(getCurPos()+", indentation error");
                    }
                    break;
            }
        }

        return true;
    }

    private static ArrayList<ArrayLine> produceArrayLines(ArrayList<String> content) throws Exception
    {
        ArrayList<ArrayLine> arrayLines = new ArrayList<>();
        for(String s: content)
        {
            columnNum = 0;
            char[] chars = s.toCharArray();
            int indentation = 0;

            while(chars[columnNum]==' ')
            {
                indentation++;
                columnNum++;
            }

            if(indentation%2!=0)
                throw new Exception(getCurPos()+", indentation error");

            //type 0
            if(chars[columnNum]=='-' && s.length()!=columnNum+1)
            {
                columnNum++;

                if(chars[columnNum]==' ')
                {
                    columnNum++;
                    YmlItem item = getValue(s, null, columnNum, s.length() - 1);
                    String value = item.getValue();
                    ArrayLine arrayLine = new ArrayLine(0, indentation, value);
                    arrayLines.add(arrayLine);
                }
                else
                {
                    throw new Exception(getCurPos()+", blank space expected");
                }
            }
            //type 1
            else if(chars[columnNum]=='-' && s.length()==columnNum+1)
            {
                ArrayLine arrayLine = new ArrayLine(1, indentation, null);
                arrayLines.add(arrayLine);
            }
            //type 2 and 3
            else if(Validator.getInstance().isAscii(chars[columnNum]))
            {
                String key = getKey(s, columnNum);

                //type 2
                if(columnNum+1 == s.length())
                {
                    ArrayLine arrayLine = new ArrayLine(2, indentation, key);
                    arrayLines.add(arrayLine);
                }
                //type 3
                else
                {
                    columnNum += 2;
                    YmlItem item = getValue(s, null, columnNum, s.length()-1);
                    String value = item.getValue();
                    ArrayLine arrayLine = new ArrayLine(3, indentation, key+":"+value);
                    arrayLines.add(arrayLine);
                }
            }
            else
                throw new Exception(getCurPos()+", - or acsii needed");
        }

        return arrayLines;
    }

    private static void produceArrayItem(ArrayList<ArrayLine> arrayLines, YmlArrayItem arrayItem) throws Exception
    {
        Stack<YmlItem> stack = new Stack<>();
        stack.push(arrayItem);
        for(int i = 0; i < arrayLines.size(); i++)
        {
            ArrayLine arrayLine = arrayLines.get(i);

            if(arrayLine.type == 0)
            {
                columnNum = 0;

                YmlItem item = getValue(arrayLine.value, null, 0, arrayLine.value.length()-1);
                item.setInAnArray(true);
                stack.peek().addItem(item);

                if(arrayLines.size() == i+1)
                {
                    while(stack.size()!=1)
                    {
                        YmlItem item1 = stack.pop();
                        stack.peek().addItem(item1);
                    }
                }
                else if(arrayLine.indentation > arrayLines.get(i+1).indentation && stack.size() != 1)
                {
                    YmlItem item1 = stack.pop();
                    stack.peek().addItem(item1);
                }
            }
            else if(arrayLine.type == 1)
            {
                int j = i+1;
                ArrayLine newArrayLine = arrayLines.get(j);
                if(newArrayLine.indentation!=arrayLine.indentation+2)
                    throw new Exception(getCurPos()+", indentation error");

                if(newArrayLine.type==0||newArrayLine.type==1)
                {
                    YmlArrayItem arrayItem2 = new YmlArrayItem(null, true);
                    stack.push(arrayItem2);
                }
            }
            else if(arrayLine.type==2)
            {
                if(arrayLines.get(i+1).indentation!=arrayLine.indentation+2)
                    throw new Exception(getCurPos()+", indentation error");

                if(arrayLines.get(i+1).type==0|| arrayLines.get(i+1).type==1)
                {
                    YmlArrayItem item = new YmlArrayItem(arrayLine.value, true);
                    stack.push(item);
                }
                else
                {
                    YmlYmlItem item = new YmlYmlItem(arrayLine.value, true);
                    stack.push(item);
                }
            }
            else if(arrayLine.type == 3)
            {
                columnNum = 0;
                String key = getKey(arrayLine.value, 0);
                columnNum ++;
                YmlItem item = getValue(arrayLine.value, key, columnNum, arrayLine.value.length()-1);
                item.setInAnArray(true);

                stack.peek().addItem(item);

                if(arrayLines.size()==i+1)
                {
                    while (stack.size() != 1)
                    {
                        YmlItem item1 = stack.pop();
                        stack.peek().addItem(item1);
                    }
                }
                else if(arrayLine.indentation > arrayLines.get(i+1).indentation && stack.size() != 1)
                {
                    YmlItem item1 = stack.pop();
                    stack.peek().addItem(item1);
                }
            }
        }

        JsonFile.getInstance().addItem(arrayItem);
    }
}

class Validator{
    private static Validator singleton;

    private Validator(){}

    public static Validator getInstance()
    {
        if(singleton==null)
            singleton = new Validator();

        return singleton;
    }

    public boolean isaToz(char c)
    {
        return ('a'<=c && c<='z');
    }

    public boolean isAToZ(char c)
    {
        return ('A'<=c && c<='Z');
    }

    public boolean is0To9(char c)
    {
        return ('0'<=c && c<='9');
    }

    public boolean is1To9(char c)
    {
        return ('1'<=c && c<='9');
    }

    public boolean isAscii(char c)
    {
        int ascii = (int) c;

        return (32<=ascii && ascii<=126);
    }
}

class ArrayLine
{
    /**
     *  type 0: - value
     *  type 1: -
     *  type 2: key:
     *  type 3: key: value
     */
    int type;
    int indentation;
    String value;

    public ArrayLine(int type, int indentation, String value)
    {
        this.type = type;
        this.indentation = indentation;
        this.value = value;
    }
}