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

import java.util.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "category")
public class ASTERIXCategory
{
    private String cat;
    private String ver;
    private List<ASTERIXDataItem> di;
    
    @XmlAttribute
    public void setNo(String cat) { this.cat = cat; }
    @XmlAttribute
    public void setVer(String ver) { this.ver = ver; }
    
    public String getNo() { return this.cat; }
    public String getVer() { return this.ver; }
    
    @XmlElement(name = "dataitem")
    public void setDi(List<ASTERIXDataItem> di) { this.di = di; }
    public List<ASTERIXDataItem> getDi() { return this.di; }
}