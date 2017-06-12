int printf(char *, ...);

typedef struct userInfo {
	char id;
	char *name;
	char *mailAddr;
	char *url;
} UserInfo;

typedef struct class {
	char *name;
	double hashCode;
	char ch;
	int classCode;
} Class;

typedef struct basicType {
	signed char s_c;
	unsigned char u_c;
	
	signed short s_s;
	unsigned short u_s;
	
	signed int s_i;
	unsigned int u_i;
	
	signed long s_l;
	unsigned long u_l;
	
	float f;
	double d;
} BasicType;

typedef struct arrays {
	unsigned char carray[10];
	short sarray[10];
	int iarray[10];
	long larray[10];
	float farray[10];
	double darray[10];
} Arrays;

typedef struct charArray {
	char contents[20];
} CharArray;

typedef struct nestedStruct {
	UserInfo userInfo;
	BasicType basicType;
} NestedStruct;

UserInfo userInfo1;
Class class1;
BasicType basicType1;
Arrays arrays1;
CharArray charArray1;
NestedStruct nestedStruct1;

UserInfo arrayUserInfo[61];
Class arrayClass[3][8];
BasicType arrayBasicType[999];
Arrays arrayArrays[32][9];
CharArray arrayCharArray[23][9][67];
NestedStruct arrayNestedStruct[5];

int main() {
	printf("%d,%d,%d,%d,%d,%d",
		sizeof(arrayUserInfo),
		sizeof(arrayClass),
		sizeof(arrayBasicType),
		sizeof(arrayArrays),
		sizeof(arrayCharArray),
		sizeof(arrayNestedStruct));

	return 0;
}

