## ASTERIXDecoder Release 3.17.3
- - -
> ASTERIXDecoder is a java library for decoding/parsing of byte array data which is compliant with EUROCONTROL's ASTERIX specification.

#### Features
* Easy to use
* Intuitive/Simple API
* Do not need extra 3rd party library

#### Supported ASTERIX Category 
* Cat 010
* Cat 011
* Cat 020
* Cat 021
* Cat 034
* Cat 048
* Cat 062
* (You can add other categories by simply adding your own xml files)

 #### How to use
<pre>
1. Add library to your CLASSPATH and declare Import Library

    import net.aviation.yts.asterix.*;
</pre>

<pre>
2. Create decoder instance of ASTERIXDecoder class

    ASTERIXDecoder decoder = new ASTERIXDecoder();

</pre>

<pre>
3. Call decode method and decoder instance returns object of List< Map< String,String > > type.

    byte[] data = .. ; // Data you want to decode 
    List< Map< String,String > > result = decoder.decode(data);

</pre>
#### License
<pre>
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</pre>
