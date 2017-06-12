/* tpbsearch.c */

#include <stdio.h>

typedef struct {
  int key;
  int element;
} DataRec;
DataRec table[100 ];

 
int bSearch(        
  DataRec pTbl[],   
  int     pKey,     
  int     pCount)   
{                   
  int lLow,         
      lMid,         
      lHi;          
  
  printf("bSearch key %d tbl %d %d ", pKey, pTbl[0].key, pTbl[1].key);
  lLow = 0;              
  lHi  = pCount-1;       
  while (lLow <= lHi) {  
    lMid = (lLow + lHi) / 2;      
    if (pKey < pTbl[lMid].key)    
      lHi = lMid - 1;             
    else                          
      lLow = lMid + 1;            
  }
   
   
   
  if ((lHi > 0)&&(pKey == pTbl[lHi].key))
    return lHi;     
  else 
    return -1;      
}  

 
int bSearchAndInsertIfNotFound(  
  DataRec  pTbl[],  
  DataRec *pData,   
  int     *pCount)  
{                   
  int lLow, lMid, lHi, li;
  
  printf("bSearchAndInsertIfNotFound key %d data %d tbl %d ", pData->key, 
         pData->element, pTbl[0].key);
  lLow = 0;              
  lHi  = *pCount-1;
  while (lLow <= lHi) {  
    lMid = (lLow + lHi) / 2;      
    if (pData->key < pTbl[lMid].key)    
      lHi = lMid - 1;             
    else                          
      lLow = lMid + 1;            
  }
  if ((lHi > 0)&&(pData->key == pTbl[lHi].key))
    return lHi;     
  else {            
    if (*pCount >=  100 )
      return -1;                   
    else {
      for (li = *pCount; li >= lHi+1; li--) {  
        pTbl[li].key     = pData->key;           
        pTbl[li].element = pData->element;
      }
      pTbl[lHi+1].key     = pData->key;        
      pTbl[lHi+1].element = pData->element;
      *pCount = *pCount + 1;                     
    }
  }
  return lHi+1;
}  

main()
{
  int     li, lk, lCount;
  int     searchKey[4] = {10, 450, 605, 100 };
  DataRec additional = { 65, 650 };
  
  for (li = 0; li < 100 ; li++) {   
    table[li].key     = li * 10;
    table[li].element = li * 100;
  }
  for (li = 0; li < 4; li++) {
    lk = bSearch(table, searchKey[li], 100 );
    if (lk >= 0)
      printf("inx %d key %d elem %d \n", lk, table[lk].key, table[lk].element);
    else
      printf("key %d not found %d %d \n", searchKey[li], 
             searchKey[0], searchKey[1]);
  }
  lCount = 10;
  lk = bSearchAndInsertIfNotFound(table, &additional, &lCount);
  printf("Insert if not found %d at %d \n", additional.key, lk);
  return 0;
}

