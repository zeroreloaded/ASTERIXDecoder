/****************************************************************************

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    
    @author T.Y.Shin (https://github.com/zeroreloaded)
    
******************************************************************************/
package net.aviation.yts.asterix;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;
import javax.xml.bind.*;
import java.io.*;
import java.text.*;

public class ASTERIXDecoderUtils
{
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
	
    public ASTERIXCategory getASTERIXCategoryBySpec(String spec)
    {
        if(spec == null) return null;
        
        try
        {
            JAXBContext context = JAXBContext.newInstance(ASTERIXCategory.class);
            Unmarshaller umarshaller = context.createUnmarshaller();
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(spec);
            ASTERIXCategory category = (ASTERIXCategory)umarshaller.unmarshal(is); //new File(spec));
            
            return category;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
    private StringBuffer makeString(StringBuffer sb, int val)
    {
        sb.append(val);
        return sb;    
    }
    
    private StringBuffer makeTargetIdentification(StringBuffer sb, int val)
    {
        if(val == 0) return sb;
        else if(val == 32)
        {
            sb.append(" ");
            return sb;
        }
        else if(val >= 1 && val <= 26)
        {
            sb.append(Character.toChars(64 + val));
            return sb;
        }
        else if(val >= 48 && val <= 57)
        {
            sb.append(Character.toChars(val));
            return sb;
        }
        else return sb;
        
        /*------------- IACO Annex 10. Volume 4. Table 3.8 ------------
        b6  b5  b4  b3  b2  b1      Dec
        0   0   0   0   0   0   
        0   0   0   0   0   1   A   1
        0   0   0   0   1   0   B   2
        0   0   0   0   1   1   C   3
        0   0   0   1   0   0   D   4
        0   0   0   1   0   1   E   5
        0   0   0   1   1   0   F   6
        0   0   0   1   1   1   G   7
        0   0   1   0   0   0   H   8
        0   0   1   0   0   1   I   9
        0   0   1   0   1   0   J   10
        0   0   1   0   1   1   K   11
        0   0   1   1   0   0   L   12
        0   0   1   1   0   1   M   13
        0   0   1   1   1   0   N   14
        0   0   1   1   1   1   O   15
        0   1   0   0   0   0   P   16
        0   1   0   0   0   1   Q   17
        0   1   0   0   1   0   R   18
        0   1   0   0   1   1   S   19
        0   1   0   1   0   0   T   20
        0   1   0   1   0   1   U   21
        0   1   0   1   1   0   V   22
        0   1   0   1   1   1   W   23
        0   1   1   0   0   0   X   24
        0   1   1   0   0   1   Y   25
        0   1   1   0   1   0   Z   26
        1   0   0   0   0   0   SP  32
        1   1   0   0   0   0   0   48
        1   1   0   0   0   1   1   49
        1   1   0   0   1   0   2   50
        1   1   0   0   1   1   3   51
        1   1   0   1   0   0   4   52
        1   1   0   1   0   1   5   53
        1   1   0   1   1   0   6   54
        1   1   0   1   1   1   7   55
        1   1   1   0   0   0   8   56
        1   1   1   0   0   1   9   57
        
        -----------------------------*/  
    }
    
    private String zeropadding(String s, int len)
    {
        if(s.length() == len) return s;
        int dif = Math.abs(s.length() - len);
        
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<dif; i++) sb.append("0");
        
        sb.append(s);
        return sb.toString();    
    }
    
    public String byteToString(int b)
    {
        return String.format("0x%02x", b&0xff);
    }
    
    public String byteArrayToString(byte[] b)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("0x");
        for(int i=0; i<b.length; i++)
        {
            sb.append(String.format("%02x", b[i]&0xff));
        }
        
        return sb.toString();    
    }
    
    public int unsignedToInt(byte[] data)
    {
        int val = 0;
        int len = data.length;
        
        if(len > 4) throw new RuntimeException("Too big to convert to integer");
        
        for(int i=0; i<len; i++)
        {
            val = val << 8; 
            val = val | (data[i] & 0xff);
        }
        
        return val;
    }
    
    private final int signedToInt(byte[] data)
    {
    	return (new java.math.BigInteger(data)).intValue();
    }
    
    private int unsignedShortToInt(byte[] b)
    {
        int i = 0;
        
        i |= b[0] & 0xff;
        i <<= 8;
        
        i |= b[1] & 0xff;
        
        return i;    
    }
    
    private ASTERIXDataItem findASTERIXDataItem(int idx, ASTERIXCategory cat)
    {
        try
        {
            List<ASTERIXDataItem> di = cat.getDi();
            if(di == null) return null;
            String idxString = String.valueOf(idx);
            
            for(int i=0; i<di.size(); i++)
            {
                ASTERIXDataItem d = di.get(i);
                if(d.getFrn().equals(idxString))  
                {
                    return d;
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean makeUAP(Map<Integer, ASTERIXDataItem> map, int idx, int fspec, ASTERIXCategory cat)
    {
        if(map == null) map = new HashMap<Integer, ASTERIXDataItem>();
        if(idx < 0) return false;
        
        //System.out.println("makeUAP fspec: " + fspec + ", " + byteToString(fspec));
         
        fspec = fspec & 0xff;
        
        boolean extendable = false;
        if((fspec & 0x01) == 0x01) extendable = true;
        
        int comp = 0x80;
        int offset = (idx * 7) + 1;
        
        for(int i=0; i<7; i++)
        {
            if((fspec & comp) == comp) map.put(new Integer(offset), findASTERIXDataItem(offset, cat));
            comp = comp >>> 1;
            ++offset;
        }
        
        return extendable;
            
    }
    
    private int getOctet(byte[] target, int bit)
    {
        if(target == null || target.length == 0) return -1;
        
        int size = target.length;
        int msb = size * 8;
        int octet = 1;
        
        for(octet = 1; octet<=size; octet++)
        {
            msb -= 8;
            if(bit > msb) return octet;
        }
        
        return -1;
    }
    
    private String makeOctal(byte[] buf)
    {
        if(buf == null || buf.length != 2) return "";
        
        int num = buf[0] & 0xff;
        int num2 = buf[1];
        int n = buf[0] & 0x0f;
        n = n >>> 1;
        
        StringBuffer sbuffer = new StringBuffer();
        sbuffer = makeString(sbuffer, n);
        
        n = ((num2 >>> 6) & 0x03) | ((num & 0x01) << 2);
        
        sbuffer = makeString(sbuffer, n);
        
        n = ((num2 >>> 3) & 0x07);
        sbuffer = makeString(sbuffer, n);
        
        n = num2 & 0x07;
        sbuffer = makeString(sbuffer, n);
        
        return sbuffer.toString();
    }
    
    private String makeICAOCode(byte[] buf)
    {
        if(buf == null || buf.length != 6) return "";
        
        StringBuffer sb = new StringBuffer();
        
        int octet1 = buf[0];
        octet1 = octet1 & 0xff;
        
        octet1 = octet1 >>> 2;
        sb = makeTargetIdentification(sb, octet1);
        
        octet1 = buf[0];
        octet1 = octet1 & 0xff;
        int octet2 = buf[1];
        octet2 = octet2 & 0xff;
        octet2 = octet2 >>> 4;
        
        octet1 = octet1 << 4;
        octet1 = octet1 & 0x30;
        int number = octet1 | octet2;
        sb = makeTargetIdentification(sb, number);
        
        octet1 = buf[1] & 0xff;
        octet2 = buf[2] & 0xff;
        
        octet1 = octet1 << 2;
        octet1 = octet1 & 0x3f;
        octet2 = octet2 >>> 6;
        
        number = octet1 | octet2;
        sb = makeTargetIdentification(sb, number);
        
        octet1 = buf[2];
        number = octet1 & 0x3f;
        sb = makeTargetIdentification(sb, number);
        
        octet1 = buf[3];
        octet1 = octet1 & 0xff;
        number = octet1 >>> 2;
        sb = makeTargetIdentification(sb, number);
        
        octet1 = buf[3] & 0xff;
        octet2 = buf[4] & 0xff;
        
        octet1 = octet1 << 4;
        octet1 = octet1 & 0x3f;
        octet2 = octet2 >>> 4;
        
        number = octet1 | octet2;
        sb = makeTargetIdentification(sb, number);
        
        octet1 = buf[4] & 0xff;
        octet2 = buf[5] & 0xff;
        
        octet1 = octet1 << 2;
        octet1 = octet1 & 0x3f;
        octet2 = octet2 >>> 6;
        
        number = octet1 | octet2;
        sb = makeTargetIdentification(sb, number);
        
        octet1 = buf[5];
        number = octet1 & 0x3f;
        sb = makeTargetIdentification(sb, number);
        
        return sb.toString();
    }
    
    public String makeAscii(byte[] buf)
    {
        if(buf == null || buf.length == 0) return "";
        
        return new String(buf);
    }
    
    private String zeroPadding2digitAtFront(int number)
    {
        if(number <= 0) return "01";
        try
        {
            String str = String.valueOf(number);
            int length = str.length();

            if(length >= 2) return str;
            else return "0" + str;
        }
        catch(Exception e)
        {
            return "01";
        }
    }

    public int makeResultFixed(Map<String, String> map, ASTERIXDataItem di, byte[] data, int index)
    {
        if(di == null || data == null || index < 0) return -1;
        
        int size = 0;

        try
        {
        	size = Integer.parseInt(di.getLength());
        	if(size < 0) return -1;
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	return -1;
        }
        byte[] target = new byte[size];
        
        try
        {
            System.arraycopy(data, index, target, 0, size);
        }
        catch(Exception e)
        {
        	return -1;    
        }
        
        index += size;
        
        List<ASTERIXStructure> st = di.getASTERIXStructure();
                
        for(int i=0; i<st.size(); i++)
        {
            ASTERIXStructure struc = st.get(i);
            String name = struc.getName().toUpperCase();
            if(name == null || name.trim().length() == 0) name = "unknown";
            
            int frombit = Integer.parseInt(struc.getFrombit());
            int tobit = Integer.parseInt(struc.getTobit());
            int bit_length = frombit - tobit;
            int from_octet = getOctet(target, frombit);
            int to_octet = getOctet(target, tobit);
            
            if(from_octet == -1 || to_octet == -1) continue;
            
            ASTERIXMessageTypeMap mtype = struc.getMessageType();
            String unit = struc.getUnit();
            String scale = struc.getScale();
            
            if(scale == null || scale.trim().length() == 0) scale = "1";
            
            String format = struc.getFormat();
            float no = 0;
            byte[] bnum = null;
            boolean is_string = false;
            
            if(mtype == null)
            {
                String mustbe = struc.getMustbe();
                if(mustbe != null)
                {
                    if(Integer.parseInt(struc.getMustbe()) == 0)
                    {
                        map.put(name, "0");
                        continue;
                    }
                }
                        
                if(from_octet == to_octet)
                {
                    int number = target[to_octet - 1] & 0xff;
                    int shift = (8*size) - frombit;

                    number = number << shift;
                    number = number & 0xff;
                    number = number >>> (8 - ((size*8)-(tobit+shift))- 1); 
                    Integer itg = new Integer(number);

                    no = (float)number;
                }
                else
                {
                    int byte_size = to_octet - from_octet;
                    
                    if(to_octet != from_octet) byte_size += 1;
                    bnum = new byte[byte_size];
                    System.arraycopy(target, from_octet-1, bnum, 0, byte_size);
                    
                    is_string = false;
                    
                    if(format.trim().equalsIgnoreCase("uint")) no = (float)unsignedToInt(bnum);
                    else if(format.trim().equalsIgnoreCase("int")) 
                    {
                        if(bit_length < 16 && bit_length % 16 != 0)
                        {
                            int shift = 16 - bit_length - 1;
                            bnum[0] = (byte)(bnum[0] << shift);
                            bnum[0] = (byte)(bnum[0] & 0xff);
                            bnum[0] = (byte)(bnum[0] >>> shift);
                            bnum[0] = (byte)(bnum[0] & 0xff);

                        }
                        no = (float)signedToInt(bnum);
                    }
                    else if(format.trim().equalsIgnoreCase("octal")) 
                    {
                        map.put(name, makeOctal(bnum));
                        is_string = true;
                    }
                    else if(format.trim().equalsIgnoreCase("icaocode")) 
                    {
                        map.put(name, makeICAOCode(bnum));
                        is_string = true;
                    }
                    else if(format.trim().equalsIgnoreCase("string"))
                    {
                        map.put(name, byteArrayToString(bnum));
                        is_string = true;
                        
                    }
                    else if(format.trim().equalsIgnoreCase("ascii")) 
                    {
                        map.put(name, makeAscii(bnum));
                        is_string = true;
                    }
                    else no = (float)-99.9;
                }
                
                if(!is_string)
                {
                    no *= Float.parseFloat(scale);
                    map.put(name, String.valueOf(no));
                }
            }
            else
            {
                Map<Integer, String> mm = mtype.getMessageMap();
                
                if(from_octet == to_octet)
                {
                    int number = target[to_octet - 1] & 0xff;
                    int shift = (8*size) - frombit;
                    number = number << shift;
                    number = number & 0xff;
                    number = number >>> (8 - ((size*8)-(tobit+shift))- 1); 

                    Integer itg = new Integer(number);
                    
                    if(mm.get(itg) != null)
                    {
                        map.put(name, mm.get(itg));
                    } 
                    else map.put(name, "unknown");
                }
                else
                {
                    int byte_size = from_octet - to_octet;
                    byte[] number = new byte[byte_size];
                    
                    System.arraycopy(target, from_octet, target, 0, size);
                    
                    no = (float)unsignedToInt(number);
                }
            }
        }
        
        return index;
    }
    
    public boolean makeCompoundUAP(Map<Integer, ASTERIXDataItem> map, int idx, int fspec, String frn, ASTERIXCategory category)
    {
        if(map == null) map = new HashMap<Integer, ASTERIXDataItem>();
        if(idx < 0) return false;
        
        fspec = fspec & 0xff;
        
        boolean extendable = false;
        if((fspec & 0x01) == 0x01) extendable = true;
        
        int comp = 0x80;
        int offset = (idx * 7) + 1;
        
        for(int i=0; i<7; i++)
        {
            if((fspec & comp) == comp) 
            {
                String key = frn + zeroPadding2digitAtFront(offset);
                map.put(new Integer(key), findASTERIXDataItem(Integer.parseInt(key), category));
            }
            comp = comp >>> 1;
            ++offset;
        }
        
        return extendable;
            
    }
    
    public void makeVariableUAP(Map<Integer, ASTERIXDataItem> map, String di_frn, ASTERIXCategory category, byte[] data, int index)
    {
        if(map == null) map = new HashMap<Integer, ASTERIXDataItem>();
        
        int comp = 0x01;
        int offset = 1;
        int idx = index;

        while(true)
        {
            try
            {
                String key = di_frn + zeroPadding2digitAtFront(offset);
                
                ASTERIXDataItem di = findASTERIXDataItem(Integer.parseInt(key), category);
                if(di == null) break;
                map.put(new Integer(key), di);

                int data_length = Integer.parseInt(di.getLength());
                idx = idx + data_length;
                int next = data[idx-1] & 0xff;

                if((next & comp) != comp) break; 
                ++offset;
            }
            catch(Exception e)
            {
                break;
            }
        }
    }

    public int makeResultRepetitive(Map<String, String> map, ASTERIXDataItem di, byte[] data, int index, ASTERIXCategory category)
    {
        int di_len = 0;
        
        try
        {
            di_len = Integer.parseInt(di.getLength());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return -1;
        }
        
        try
        {
            List<ASTERIXStructure> st = di.getASTERIXStructure();
            ASTERIXStructure s = st.get(0);
            if(s == null) return  index + di_len;
            
            int rep = data[index] & 0xff;
            ++index;
            Map<String, String> result_map = new HashMap<String, String>();
            di.setLength(String.valueOf(di_len-1));
	    di.setFormat("fixed");

            for(int i=0; i<rep; i++)
            {
                index = makeResult(result_map, di, data, index, category);
            }

            Iterator<String> iterator = result_map.keySet().iterator();
            while(iterator.hasNext())
            {
                String key = iterator.next();
                
                map.put(key, result_map.get(key));
            }

            return index;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return 0;
        }
    }

    private void printDataAsHex(byte[] data, int index)
    {
        int dataLength = data.length;
        int targetLength = dataLength - index;
        byte[] tmp = new byte[targetLength];
        System.arraycopy(data, index, tmp, 0, targetLength);

        System.out.println("Data as Hex ===========> " + byteArrayToString(tmp));
    }

    public int makeResultVariable(Map<String, String> map, ASTERIXDataItem di, byte[] data, int index, ASTERIXCategory category)
    {
        try
        {
            String di_frn = di.getFrn();
            int idx = 0;
            Map<Integer, ASTERIXDataItem> uap = new LinkedHashMap<Integer, ASTERIXDataItem>();
            
            makeVariableUAP(uap, di_frn, category, data, index);
            
            Iterator<Integer> it = uap.keySet().iterator();
            Map<String, String> result_map = new HashMap<String, String>();
            
            while(it.hasNext())
            {
                Integer frn = it.next();
                ASTERIXDataItem _di = uap.get(frn);
                index = makeResult(result_map, _di, data, index, category);
                if(index == -1) break;
            }
            
            Iterator<String> iterator = result_map.keySet().iterator();
            while(iterator.hasNext())
            {
                String key = iterator.next();
                
                map.put(key, result_map.get(key));
            } 

            return index;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public int makeResultCompound(Map<String, String> map, ASTERIXDataItem di, byte[] data, int index, ASTERIXCategory category)
    {
        try
        {
            String di_frn = di.getFrn();
            int idx = 0;
            Map<Integer, ASTERIXDataItem> uap = new LinkedHashMap<Integer, ASTERIXDataItem>();
            
            while(true)
            {
                int fspec = data[index+idx] & 0xff;
                if(!makeCompoundUAP(uap, idx, fspec, di_frn, category)) break;
                ++idx;
            }
            
            index += (idx+1);
            
            Iterator<Integer> it = uap.keySet().iterator();
            Map<String, String> result_map = new HashMap<String, String>();
            
            while(it.hasNext())
            {
                Integer frn = it.next();
                ASTERIXDataItem _di = uap.get(frn);
                index = makeResult(result_map, _di, data, index, category);
                if(index == -1) break;
            }
            
            Iterator<String> iterator = result_map.keySet().iterator();
            while(iterator.hasNext())
            {
                String key = iterator.next();
                
                map.put(key, result_map.get(key));
            } 
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            index = -1;
        }
        
        return index;
    }
    
    public int makeResult(Map<String, String> map, ASTERIXDataItem di, byte[] data, int index, ASTERIXCategory category)
    {
        if(di == null || data == null || data.length == 0) return -1;
        if(map == null) map = new HashMap<String, String>();
        
        if(di.getFormat().trim().equalsIgnoreCase("fixed")) return makeResultFixed(map, di, data, index);
        else if(di.getFormat().trim().equalsIgnoreCase("variable")) return makeResultVariable(map, di, data, index, category);
        else if(di.getFormat().trim().equalsIgnoreCase("repetitive")) return makeResultRepetitive(map, di, data, index, category);
        else if(di.getFormat().trim().equalsIgnoreCase("compound")) return makeResultCompound(map, di, data, index, category);
        else return -1;
    }
}
