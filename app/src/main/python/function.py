from deep_translator import GoogleTranslator
import detectlanguage
detectlanguage.configuration.api_key = "1b3ff151773d9ef32f7365a0929ecc0d"
def type(text):
    try:
        result = detectlanguage.detect(text)
        return result[0]['language']
    except:
        return "Something went wrong"
def language(text):
    dic=GoogleTranslator().get_supported_languages(as_dict=True)
    try:
        result = detectlanguage.detect(text)[0]['language']
        for key,value in dic.items():
            if(value==result):
                return key
    except:
      return "Can't detect"
def setString(a,b,c):
    try:
     return GoogleTranslator(source=a, target=b).translate(c)
    except:
        return "Something Went Wrong"
def get_language_key(language):
    languages=GoogleTranslator().get_supported_languages(as_dict=True)
    return languages[language.lower()]
def pdf_trans(path):
    try:
     result=GoogleTranslator().translate_file(path)
     return result
    except Exception as ecx:
     return str(ecx)


