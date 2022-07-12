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

@XmlRootElement(name = "structure")
public class ASTERIXStructure
{
    private String octet;
    private String frombit;
    private String tobit;
    
    private String name;
    private String format;
    private String unit;
    private String scale;
    private String desc;
    private ASTERIXMessageTypeMap map;
    private String mustbe;
    
    @XmlAttribute(name = "octet")
    public void setOctet(String octet) { this.octet = octet; }
    public String getOctet() { return this.octet; }
    
    @XmlAttribute(name = "frombit")
    public void setFrombit(String fb) { this.frombit = fb; }
    public String getFrombit() { return this.frombit; }
    
    @XmlAttribute(name = "tobit")
    public void setTobit(String tb) { this.tobit = tb; }
    public String getTobit() { return this.tobit; }
    
    @XmlElement(name="name")
    public void setName(String name) { this.name = name; }
    public String getName() { return this.name; }
    
    @XmlElement(name="format")
    public void setFormat(String format) { this.format = format; }
    public String getFormat() { return this.format; }
    
    @XmlElement(name="unit")
    public void setUnit(String unit) { this.unit = unit; }
    public String getUnit() { return this.unit; }
    
    @XmlElement(name="scale")
    public void setScale(String scale) { this.scale = scale; }
    public String getScale() { return this.scale; }
    
    @XmlElement(name="desc")
    public void setDesc(String desc) { this.desc = desc; }
    public String getDesc() { return this.desc; }
    
    @XmlElement(name="codes")
    public void setMessageType(ASTERIXMessageTypeMap mm) { this.map = mm; }
    public ASTERIXMessageTypeMap getMessageType() { return this.map; }
    
    @XmlElement(name="mustbe")
    public void setMustbe(String s) { this.mustbe = s; }
    public String getMustbe() { return this.mustbe; }
    
}