/*
   "ClassInfo"  Java class file -> infomation file

   files:
   classinfo.c
   bytecode.h
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "bytecode.h"

/* ---------- Macros ---------- */
#define PD(s,l) PrintDigit(in,out,s,l)
#define PI(s,l) PrintIndex(in,out,s,l)
#define PF(s,l) PrintFlags(in,out,s,l)

/* ---------- Index to String ---------- */
char NameIndex[256][32];
unsigned char CP_Buf[32000];
int CP_Max,CP_Link[1000];
int dump_f;

/* ---------- Prototype Difinition of Functions ----------*/
void DumpFile(FILE *,FILE *);
void MakeInfo(FILE *,FILE *);
void GetCP_Buf(FILE *,FILE *,int);
void GetConstantPool(FILE *,int);
void SearchAndGetCP(FILE *,int,int);
void GetInterfaces(FILE *,FILE *,int);
void GetFields(FILE *,FILE *,int);
void GetConstantValueAttribute(FILE *,FILE *,int);
void GetMethods(FILE *,FILE *,int);
void GetCodeOrExceptionAttribute(FILE *,FILE *,int);
void GetLineNumberTableOrLocalVariableAttribute(FILE *,FILE *,int);
void GetClassAttributes(FILE *,FILE *,int);
void GetByteCode(FILE *,FILE *,int);
long GetDigit(int,FILE *);
int  PrintDigit(FILE *,FILE *,char *,int);
int  PrintIndex(FILE *,FILE *,char *,int);
int  PrintFlags(FILE *,FILE *,char *,int);

/* ---------- Main Function ---------- */
void main(int argc,char *argv[])
{
  int i,j;
  FILE	*in,*out;
  char 	magic[5];
  char 	check[5]={0xca,0xfe,0xba,0xbe,0x00};
  char	classfile[256],infofile[256];
  
  /* Get Arguments */
  dump_f = 0;
  j = 0;
  for(i=1;i<argc;i++){
    if(strcmp(argv[i],"-d") == 0){
      dump_f = 1;
    }else{
      if(j==0){
	strcpy(classfile,argv[i]);
	j++;
      }else{
	strcpy(infofile,argv[i]);
	j++;
      }
    }
  }
  if(j!=2){
    printf("classinfo -d [JavaClassfile] [Infomationfile]\n");
    exit(1);
  }
   
  /* Open Files */
  if((in = fopen(classfile,"rb"))==NULL){ /* open class file */
    printf("can't open class file.");
    exit(1);
  }
  if((out = fopen(infofile,"wt"))==NULL){ /* open infomation file */
    printf("can't open infomation file.");
    exit(1);
  }
  
  /* Dump class file */
  if(dump_f==1){
    fprintf(out,"#Dump of Java class file.\n");
    DumpFile(in,out);
  }

  /* Check ClassFile Header(0xCAFEBABE)*/
  fgets(magic,5,in);
  if(strcmp(magic,check)!=0){
    printf("'%s' is not class file.",classfile);
    exit(1);
  }

  printf("#'%s' -> '%s'\n",classfile,infofile);
  
  /* Make Infomation */
  fprintf(out,"#Infomation of Java class file.\n");
  fprintf(out,"sourcefile '%s'\n",classfile);
  MakeInfo(in,out);
  
  /* Close Files */
  fclose(in);
  fclose(out);
}

/* ----------- Make Infomation ---------- */
void MakeInfo(FILE *in,FILE *out)
{
  long tmp;
  
  printf("#Versions\n");
  PD("minor_version\t\t\t",2); /* Minor Version */
  PD("major_version\t\t\t",2); /* Major Version */

  printf("#ConstantPool\n");
  tmp = PD("constant_pool_count\t\t",2); /* Get constant_pool_count */ 
  GetCP_Buf(in,out,tmp);
  GetConstantPool(out,tmp); /* Get constant_pool */ 
  
  PF("access_flag\t\t\t",2);/* Get access_flag */
  PI("this_class\t\t\t",2); /* Get this_class */
  PI("super_class\t\t\t",2); /* Get super_class */

  printf("#Interfaces\n");
  tmp = PD("interfaces_count\t\t",2); /* Get interfaces_count */
  GetInterfaces(in,out,tmp); /* Get interfaces */

  printf("#Fields\n");
  tmp = PD("fields_count\t\t\t",2);/* Get fields_count */ 
  GetFields(in,out,tmp); /* Get fields */

  printf("#Methods\n");
  tmp = PD("methods_count\t\t\t",2); /* Get methods_count */
  GetMethods(in,out,tmp); /* Get methods */

  printf("#ClassAttributes\n");
  tmp = PD("attributes_count\t\t",2); /* Get attributes_count */
  GetClassAttributes(in,out,tmp); /* Get class_attributes */  
}

/* ---------- Get Constant Pool ---------- */
void GetCP_Buf(FILE *in,FILE *out,int count)
{
  int i,c,tmp;
  int tag;
  int link;

  c = 1;
  link = 1;
  for(i=0;i<count-1;i++){
    tag = fgetc(in);
    CP_Link[link++] = c;
    c ++;
    switch(tag){
    case 1: /* CONSTANT_Utf8 */
      tmp = GetDigit(2,in);
      c += 2;
      break;
    case 3: /* CONSTANT_Integer */
      tmp = 4;
      break;
    case 4: /* CONSTANT_Float */
      tmp = 4;
      break;
    case 5: /* CONSTANT_Long */
      tmp = 8;
      CP_Link[link] = -1;
      link++;
      i++;
      break;
    case 6: /* CONSTANT_Double */
      tmp = 8;
      CP_Link[link] = -1;
      link++;
      i ++;
      break;
    case 7: /* CONSTANT_Class */
      tmp = 2;
      break;
    case 8: /* CONSTANT_String */
      tmp = 2;
      break;
    case 9: /* CONSTANT_Fieldref */
      tmp = 4;
      break;
    case 10: /* CONSTANT_Methodref */
      tmp = 4;
      break;
    case 11: /* CONSTANT_InterfaceMethodref */
      tmp = 4;
      break;
    case 12: /* CONSTANT_NameAndType */
      tmp = 4;
      break;
    }
    fseek(in,tmp,1);
    c += tmp;
  }
  
  CP_Max = link;

  fseek(in,-(c-1),1);
  for(i=1;i<c;i++)CP_Buf[i]=fgetc(in); /* ConstantPool -> CP_Buf */
}

/* ---------- Search And Get Constant Pool ---------- */
/*
   type
   0:Nomal
   1:No title
*/
void SearchAndGetCP(FILE *out,int no,int type)
{
  int i,j,c;
  unsigned char tag;
  long tmp;
  char str[256];

  c = CP_Link[no];
  if(c == -1)return;
  tag = CP_Buf[c++];
  if(type==0)fprintf(out,"%03d:",no);
  switch(tag){
  case 1: /* CONSTANT_Utf8 */
    if(type==0)fprintf(out,"CONSTNT_Utf8\t\t");
    tmp = (CP_Buf[c]<<8)|CP_Buf[c+1];
    for(j=0;j<tmp;j++)str[j]=CP_Buf[c+j+2];
    str[j] = '\0';
    fprintf(out,"'%s'",str);
    if(type==0)fprintf(out,"\n");
    strcpy(NameIndex[no],str);
    break;
  case 3: /* CONSTANT_Integer */
    tmp = (CP_Buf[c]<<24)|(CP_Buf[c+1]<<16)|(CP_Buf[c+2]<<8)|CP_Buf[c+3];
    fprintf(out,"CONSTNT_Integer\t\t%d(0x%08x)\n",tmp,tmp);
    break;
  case 4: /* CONSTANT_Float */
    tmp = (CP_Buf[c]<<24)|(CP_Buf[c+1]<<16)|(CP_Buf[c+2]<<8)|CP_Buf[c+3];
    fprintf(out,"CONSTNT_Float\t\t%d(0x%08x)\n",tmp,tmp);
    break;
  case 5: /* CONSTANT_Long */
    fprintf(out,"CONSTNT_Long\n");
    tmp = (CP_Buf[c]<<24)|(CP_Buf[c+1]<<16)|(CP_Buf[c+2]<<8)|CP_Buf[c+3];
    fprintf(out,"\thigh\t\t\t%ld(0x%08x)\n",tmp,tmp);
    tmp = (CP_Buf[c+4]<<24)|(CP_Buf[c+5]<<16)|(CP_Buf[c+6]<<8)|CP_Buf[c+7];
    fprintf(out,"\tlow\t\t\t%ld(0x%08x)\n",tmp,tmp);
    i++;
    break;
  case 6: /* CONSTANT_Double */
    fprintf(out,"CONSTNT_Double\n");
    tmp = (CP_Buf[c]<<24)|(CP_Buf[c+1]<<16)|(CP_Buf[c+2]<<8)|CP_Buf[c+3];
    fprintf(out,"\thigh\t\t\t%ld(0x%08x)\n",tmp,tmp);
    tmp = (CP_Buf[c+4]<<24)|(CP_Buf[c+5]<<16)|(CP_Buf[c+6]<<8)|CP_Buf[c+7];
    fprintf(out,"\tlow\t\t\t%ld(0x%08x)\n",tmp,tmp);
    i++;
    break;
  case 7: /* CONSTANT_Class */
    if(type==0)fprintf(out,"CONSTNT_CLass\n");
    tmp = (CP_Buf[c]<<8)|CP_Buf[c+1];
    if(type==0)fprintf(out,"\tname_index\t\t%d(0x%04x) ",tmp,tmp);
    SearchAndGetCP(out,tmp,1);
    if(type==0)fprintf(out,"\n");
    break;
  case 8: /* CONSTANT_String */
    fprintf(out,"CONSTNT_String\n");
    tmp = (CP_Buf[c]<<8)|CP_Buf[c+1];
    fprintf(out,"\tstring_index\t\t%d(0x%04x) ",tmp,tmp);
    SearchAndGetCP(out,tmp,1);
    fprintf(out,"\n");
    break;
  case 9: /* CONSTANT_Fieldref */
    fprintf(out,"CONSTNT_Fieldref\n");
    tmp = (CP_Buf[c]<<8)|CP_Buf[c+1];
    fprintf(out,"\tclass_index\t\t%d(0x%04x) ",tmp,tmp);
    SearchAndGetCP(out,tmp,1);
    fprintf(out,"\n");
    tmp = (CP_Buf[c+2]<<8)|CP_Buf[c+3];
    fprintf(out,"\tname_and_type_index\t%d(0x%04x) ",tmp,tmp);
    SearchAndGetCP(out,tmp,1);
    fprintf(out,"\n");
    break;
  case 10: /* CONSTANT_Methodref */
    fprintf(out,"CONSTNT_Methodref\n");
    tmp = (CP_Buf[c]<<8)|CP_Buf[c+1];
    fprintf(out,"\tclass_index\t\t%d(0x%04x) ",tmp,tmp);
    SearchAndGetCP(out,tmp,1);
    fprintf(out,"\n");
    tmp = (CP_Buf[c+2]<<8)|CP_Buf[c+3];
    fprintf(out,"\tname_and_type_index\t%d(0x%04x) ",tmp,tmp);
    SearchAndGetCP(out,tmp,1);
    fprintf(out,"\n");
    break;
  case 11: /* CONSTANT_InterfaceMethodref */
    fprintf(out,"CONSTNT_InterfaceMethodref\n");
    tmp = (CP_Buf[c]<<8)|CP_Buf[c+1];
    fprintf(out,"\tclass_index\t\t%d(0x%04x)\n",tmp,tmp);
    tmp = (CP_Buf[c+2]<<8)|CP_Buf[c+3];
    fprintf(out,"\tname_and_type_index\t%d(0x%04x)\n",tmp,tmp);
    break;
  case 12: /* CONSTANT_NameAndType */
    if(type==0)fprintf(out,"CONSTNT_NameAndType\n");
    tmp = (CP_Buf[c]<<8)|CP_Buf[c+1];
    if(type==0)fprintf(out,"\tclass_index\t\t%d(0x%04x) ",tmp,tmp);
    SearchAndGetCP(out,tmp,1);
    if(type==0)fprintf(out,"\n");
    tmp = (CP_Buf[c+2]<<8)|CP_Buf[c+3];
    if(type==0)fprintf(out,"\tdescriptor_index\t%d(0x%04x) ",tmp,tmp);
    SearchAndGetCP(out,tmp,1);
    if(type==0)fprintf(out,"\n");
    break;
  default: /* unknown code */
    fprintf(out,"<unknown code> TAG = 0x%02x\n",tag);      
  }  
}

/* ---------- Get Constant Pool ---------- */
void GetConstantPool(FILE *out,int count)
{
  int i;

  fprintf(out,"<constant pool>\n");
  for(i=1;i<CP_Max;i++){
    SearchAndGetCP(out,i,0);
  }
}

/* ---------- Get Interfaces ---------- */
void GetInterfaces(FILE *in,FILE *out,int count)
{
  int i;
  int tmp;

  if(count==0)return;
  
  fprintf(out,"<interfaces>\n");
  for(i=0;i<count;i++)PD("\t",2);
}

/* ---------- Get Fields ---------- */
void GetFields(FILE *in,FILE *out,int count)
{
  int i;
  int tmp;

  if(count==0)return;
  
  fprintf(out,"<fields>\n");
  for(i=0;i<count;i++){
    fprintf(out,"%03d:\n",i);
    PF("\taccess_flag\t\t",2);
    PI("\tname_index\t\t",2);
    PI("\tdescriptor_index\t",2);
    tmp = PD("\tattribute_count\t\t",2);
    GetConstantValueAttribute(in,out,tmp);
  }

}

/* ---------- Get Methods ---------- */
void GetMethods(FILE *in,FILE *out,int count)
{
  int i;
  int tmp;
  
  if(count==0)return;

  fprintf(out,"<methods>\n");
  for(i=0;i<count;i++){
    fprintf(out,"%03d:\n",i);
    PF("\taccess_flag\t\t",2);
    PI("\tname_index\t\t",2);
    PI("\tdescriptor_index\t",2);
    tmp = PD("\tattribute_count\t\t",2);
    GetCodeOrExceptionAttribute(in,out,tmp);
  }
}

/* ---------- Get Line Number Table Attribute ---------- */
void GetLineNumberTableOrLocalValiableAttribute(FILE *in,FILE *out,int count)
{
  int i,j;
  long tmp;

  if(count==0)return;

  fprintf(out,"\t<line_number_table_or_local_valiable_attribute>\n");
  for(i=0;i<count;i++){
    tmp = PI("\t\tattribute_name_index\t",2);
    if(strcmp(NameIndex[tmp],"LineNumberTable")==0){
      PD("\t\tattribute_length\t",4);
      tmp = PD("\t\tline_number_table_length\t",2);
      for(j=0;j<tmp;j++){
	PD("\t\tstart_pc\t\t",2);
	PD("\t\tline_number\t\t",2);
      }
    }else{
       PD("\t\tattribute_length\t",4);
       tmp = PD("\t\tlocal_valiable_table_length\t",2);
       for(j=0;j<tmp;j++){
	 PD("\t\tstart_pc\t\t",2);
	 PD("\t\tlength\t\t\t",2);
	 PI("\t\tname_index\t\t",2);
	 PI("\t\tdescriptor_index\t",2);
	 PD("\t\tindex\t\t",2);
       }
     }
  }
}

/* ---------- Get Code or Exception Attribute ----------- */ 
void GetCodeOrExceptionAttribute(FILE *in,FILE *out,int count)
{
  int i,j;
  long tmp;

  if(count==0)return;

  fprintf(out,"\t<code_or_exception_attribute>\n");
  for(i=0;i<count;i++){
    tmp = PI("\t\tattribute_name_index\t",2);
    if(strcmp(NameIndex[tmp],"Code")==0){
      PD("\t\tattribute_length\t",4);
      PD("\t\tmax_stack\t\t",2);
      PD("\t\tmax_local\t\t",2);
      tmp = PD("\t\tcode_length\t\t",4);
      GetByteCode(in,out,tmp);
      tmp = PD("\texception_table_length\t",2);
      for(j=0;j<tmp;j++){
	PD("\tstart_pc\t\t",2);
	PD("\tend_pc\t\t\t",2);
	PD("\thandler_pc\t\t",2);
	PD("\tcatch_type\t\t",2);
      }
      tmp = PD("\tattribute_count\t\t",2);
      GetLineNumberTableOrLocalValiableAttribute(in,out,tmp);
    }else{
      PD("\t\tattribute_length\t\t",4);
      tmp = PD("\t\tnumber_of_exceptions\t\t",2);
      for(j=0;j<tmp;j++){
	PI("\t\texception_index_table\t\t",2);
      }      
    }
  }
}

/* ---------- Get Constant Value Attribute ----------- */ 
void GetConstantValueAttribute(FILE *in,FILE *out,int count)
{
  int i;
  long tmp;

  if(count==0)return;

  fprintf(out,"\t<constantvalue_attribute>\n");
  for(i=0;i<count;i++){
    PI("\t\tattribute_name_index\t",2);
    PD("\t\tattribute_length\t",4);
    PI("\t\tconstantvalue_index\t",2);
  }
}

/* ---------- Get CLass Attributes ---------- */
void GetClassAttributes(FILE *in,FILE *out,int count)
{
  int i;
  long tmp;

  if(count==0)return;

  fprintf(out,"<class_attribute>\n");
  for(i=0;i<count;i++){
    PI("\tattribute_name_index\t",2);
    PD("\tattribute_length\t",4);
    PI("\tsourcefile_index\t",2);
  }  
}

/* ---------- Get Byte Code ---------- */
void GetByteCode(FILE *in,FILE *out,int count)
{
  int i,j;
  unsigned char tmpb;
  unsigned short tmps;
  unsigned long tmpl,tmpl2;
  unsigned char c;
  unsigned char *codestr;

  if(count==0)return;

  fprintf(out,"\t\t<code>\n");

  codestr = (unsigned char *)malloc(count);
  for(i=0;i<count;i++){
    codestr[i] = fgetc(in); 
  }

  i=0;
  while(1){
    fprintf(out,"\t\t%03d : ",i);
    c = codestr[i++]; /* get opecode */

    fprintf(out,"%s\t\t",ByteCode[c].Name);
    switch(ByteCode[c].Type){

    case 0: /* [op] */
      fprintf(out,"\n");
      break;

    case 1: /* [op][byte] */
      tmpb = codestr[i];
      i++;
      fprintf(out,"%d(0x%02x)\n",tmpb,tmpb);
      break;

    case 2: /* [op][byte1][byte2]   byye1 , byte2 */
      tmpb = codestr[i];
      i++;
      fprintf(out,"%d(0x%02x) ,",tmpb,tmpb);
      tmpb = codestr[i];
      i++;
      fprintf(out,"%d(0x%02x)\n",tmpb,tmpb);
      break;

    case 3: /* [op][byte1][byte2]   byte1<<8 | byte2 */
      tmps = (codestr[i]<<8)|codestr[i+1];
      i+=2;
      fprintf(out,"%d(0x%04x)\n",tmps,tmps);
      break;

    case 4: /* [op][byte1][byte2][byte3]   byye1<<8 | byte2 , byte3 */
      tmps = (codestr[i]<<8)|codestr[i+1];
      i+=2;
      fprintf(out,"%d(0x%04x) ,",tmps,tmps);
      tmpb = codestr[i++];
      fprintf(out,"%d(0x%02x)\n",tmpb,tmpb);
      break;

    case 5: /* [op][byte1][byte2][byte3][byte4]   byte1<<24 | byte2<<16 | byte3<<8 | byte4 */
      tmpl = (codestr[i]<<24)|((codestr[i+1]<<16)|codestr[i+2]<<8)|codestr[i+3];
      i+=4;
      fprintf(out,"%ld(0x%08x)\n",tmpl,tmpl);
      break;

    case 6: /* [op][byte1][byte2][byte3][byte4]   byte1<<8 | byte2 , byte3 , byte4 */
      tmps = (codestr[i]<<8)|codestr[i+1];
      i+=2;
      fprintf(out,"%d(0x%04x) ,",tmps,tmps);
      tmpb = codestr[i];
      i++;
      fprintf(out,"%d(0x%02x) ,",tmpb,tmpb);
      tmpb = codestr[i];
      i++;
      fprintf(out,"%d(0x%02x)\n",tmpb,tmpb);
      break;

    case 7: /* for lookupswitch */ 
      if((i%4)==3){
	tmpl = (codestr[i]<<16)|(codestr[i+1]<<8)|codestr[i+2];
	fprintf(out,"\n\t\t      \tpad:%ld(0x%06x)\n",tmpl,tmpl);
	i += 3;
      }else if((i%4)==2){
	tmpl = (codestr[i]<<8)|codestr[i+1];
	fprintf(out,"\n\t\t      \tpad:%ld(0x%04x)\n",tmpl,tmpl);
	i += 2;
      }else if((i%4)==1){
	tmpl = codestr[i];
	fprintf(out,"\n\t\t      \tpad:%ld(0x%02x)\n",tmpl,tmpl);
	i ++;
      }else fprintf(out,"\n");
      
      tmpl = (codestr[i]<<24)|((codestr[i+1]<<16)|codestr[i+2]<<8)|codestr[i+3];
      fprintf(out,"\t\t      \tdefault:%ld(0x%08x)\n",tmpl,tmpl);
      tmpl = (codestr[i+4]<<24)|((codestr[i+5]<<16)|codestr[i+6]<<8)|codestr[i+7];
      fprintf(out,"\t\t      \tnpairs:%ld(0x%08x)\n",tmpl,tmpl);
      tmpl2 = tmpl;
      i += 8;
      for(j=0;j<tmpl2;j++){
	tmpl = (codestr[i]<<24)|((codestr[i+1]<<16)|codestr[i+2]<<8)|codestr[i+3];
	fprintf(out,"\t\t      \tkey:%ld(0x%08x)\n",tmpl,tmpl);
	i += 4;
	tmpl = (codestr[i]<<24)|((codestr[i+1]<<16)|codestr[i+2]<<8)|codestr[i+3];
	fprintf(out,"\t\t      \tjunp:%ld(0x%08x)\n",tmpl,tmpl);
	i += 4;
      }
      break;

    case 8: /* for tableswitch */
      if((i%4)==3){
	tmpl = (codestr[i]<<16)|(codestr[i+1]<<8)|codestr[i+2];
	fprintf(out,"\n\t\t      \tpad:%ld(0x%06x)\n",tmpl,tmpl);
	i += 3;
      }else if((i%4)==2){
	tmpl = (codestr[i]<<8)|codestr[i+1];
	fprintf(out,"\n\t\t      \tpad:%ld(0x%04x)\n",tmpl,tmpl);
	i += 2;
      }else if((i%4)==1){
	tmpl = codestr[i];
	fprintf(out,"\n\t\t      \tpad:%ld(0x%02x)\n",tmpl,tmpl);
	i ++;
      }else fprintf(out,"\n");
      
      tmpl = (codestr[i]<<24)|((codestr[i+1]<<16)|codestr[i+2]<<8)|codestr[i+3];
      fprintf(out,"\t\t      \tdefault:%ld(0x%08x)\n",tmpl,tmpl);
      tmpl = (codestr[i+4]<<24)|((codestr[i+5]<<16)|codestr[i+6]<<8)|codestr[i+7];
      fprintf(out,"\t\t      \tlow:%ld(0x%08x)\n",tmpl,tmpl);
      tmpl = (codestr[i+8]<<24)|((codestr[i+9]<<16)|codestr[i+10]<<8)|codestr[i+11];
      fprintf(out,"\t\t      \thigh:%ld(0x%08x)\n",tmpl,tmpl);
      tmpl = (codestr[i+12]<<24)|((codestr[i+13]<<16)|codestr[i+14]<<8)|codestr[i+15];
      fprintf(out,"\t\t      \tjunp:%ld(0x%08x)\n",tmpl,tmpl);
      tmpl2 = tmpl;
      j = 20;
      i += 16;
      while(1){
	tmpl = (codestr[i]<<24)|((codestr[i+1]<<16)|codestr[i+2]<<8)|codestr[i+3];
	fprintf(out,"\t\t      \tjunp:%ld(0x%08x)\n",tmpl,tmpl);
	i += 4;
	j += 4;
	if(j >= tmpl2)break;
      }
      break;

    case 9: /* for wide */       
      tmpb = codestr[i]; /* get opecode */
      i++;
      fprintf(out,":%s\t\t",ByteCode[tmpb].Name);
      tmps = (codestr[i]<<8)|codestr[i+1];
      i+=2;
      fprintf(out,"%d(0x%04x)\n",tmps,tmps);
      break;

    default:
      fprintf(out,"\n");
      break;
    }
    if(i>=count)break;
  }

}

/* ---------- Get Digit ---------- */
long GetDigit(int l,FILE *in)
{
  int i;
  unsigned char str[256];
  long tmp = 0;
  
  fgets(str,l+1,in);
  
  for(i=0;i<l;i++){
    tmp = (tmp<<8) + str[i];
  }
  
  return tmp;
}

/* ---------- Get Digit and Print Digit ---------- */
int PrintDigit(FILE *in,FILE *out,char *str,int l)
{
  int d;
  char tmp[256],tmp2[256];

  d = GetDigit(l,in);
  strcpy(tmp,str);
  strcat(tmp,"%d(0x%");
  sprintf(tmp2,"%02dx)\n",l*2);
  strcat(tmp,tmp2);

  fprintf(out,tmp,d,d);

  return d;
}

/* ---------- Get Digit and Print Flags ---------- */
int PrintFlags(FILE *in,FILE *out,char *str,int l)
{
  int i;
  int d;
  char tmp[256],tmp2[256];

  d = GetDigit(l,in);
  strcpy(tmp,str);
  strcat(tmp,"%d(0x%");
  sprintf(tmp2,"%02dx)",l*2);
  strcat(tmp,tmp2);
  fprintf(out,tmp,d,d);

  for(i=0;i<12;i++){
    if((d&Flag[i].flag)!=0)fprintf(out," %s",Flag[i].name);
  }
  fprintf(out,"\n");

  return d;
}

/* ---------- Get Digit and Print Index  ---------- */
int PrintIndex(FILE *in,FILE *out,char *str,int l)
{
  int d;
  char tmp[256],tmp2[256];

  d = GetDigit(l,in);
  strcpy(tmp,str);
  strcat(tmp,"%d(0x%");
  sprintf(tmp2,"%02dx)",l*2);
  strcat(tmp,tmp2);
/*  strcat(tmp," '%s'\n");*/
  strcat(tmp," ");

/*  fprintf(out,tmp,d,d,NameIndex[d]);*/
  fprintf(out,tmp,d,d);
  SearchAndGetCP(out,d,1);
  fprintf(out,"\n");

  return d;
}

/* ---------- Print dump ---------- */
void DumpFile(FILE *in,FILE *out)
{
	int i;
	int c;
	
	i=0;
	fprintf(out,"          00 01 02 03 04 05 06 07  08 09 0A 0B 0C 0D 0E 0F\n",i);
	fprintf(out,"00000000  ",i);
	while(1){
		if((c=fgetc(in))==EOF)break;
		fprintf(out,"%02x ",c);
		i++;
		if((i%16)==0 && i!=0)fprintf(out,"\n%08X  ",i);
		else if(i%8==0)fprintf(out," ");
	}
	fseek(in,0,0);
	fprintf(out,"\n\n");
}

