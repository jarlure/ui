#import "Common/ShaderLib/GLSLCompat.glsllib"

#ifdef TEXTURE
    uniform sampler2D m_Texture;
    varying vec2 texCoord;
#endif

#ifdef RANGE
    uniform vec4 m_Range;
#endif

varying vec4 color;

void main() {
    #ifdef RANGE
    if(gl_FragCoord.x<m_Range.x){
        discard;
    }
    if(gl_FragCoord.y<m_Range.y){
        discard;
    }
    if(gl_FragCoord.x>m_Range.z){
        discard;
    }
    if(gl_FragCoord.y>m_Range.w){
        discard;
    }
    #endif
    #ifdef TEXTURE
      vec4 texVal = texture2D(m_Texture, texCoord);
      if(texVal.a<0.05){
        discard;
      }
      gl_FragColor = texVal * color;
    #else
      gl_FragColor = color;
    #endif
}
