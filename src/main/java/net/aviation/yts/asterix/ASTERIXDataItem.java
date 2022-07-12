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

@XmlRootElement(name = "dataitem")
public class ASTERIXDataItem
{
    private String diname;
    private String frn;
    private String format;
    private String len;
    private String mustbe;
    
    private String title;
    private String def;
    private String desc;
    private List<ASTERIXStructure> st;
    
    @XmlAttribute(name = "name")
    public void setDiName(String name) { this.diname = name; }
    @XmlAttribute(name="frn")
    public void setFrn(String frn) { this.frn = frn; }
    @XmlAttribute(name="format")
    public void setFormat(String format) { this.format = format; }
    @XmlAttribute(name="length")
    public void setLength(String len) { this.len = len; }
    @XmlAttribute(name="mustbe")
    public void setMustbe(String mb) { this.mustbe = mb; }
    
    public String getDiName() { return this.diname; }
    public String getFrn() { return this.frn; }
    public String getFormat() { return this.format; }
    public String getLength() { return this.len; }
    public String getMustbe() { return mustbe; }
    
    @XmlElement(name="title")
    public void setTitle(String title) { this.title = title; }
    @XmlElement(name="definition")
    public void setDef(String def) { this.def = def; }
    
    public String getTitle() { return this.title; }
    public String getDef() { return this.def; }
    
    @XmlElement(name = "structure")
    public void setASTERIXStructure(List<ASTERIXStructure> st) { this.st = st; }
    public List<ASTERIXStructure> getASTERIXStructure() { return this.st; }
}