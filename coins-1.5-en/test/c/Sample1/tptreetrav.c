/* tptreetrav.c */

#include <stdio.h>

void *malloc(long pSize); /* 051016 */
typedef char* String;
typedef struct treeNode* tree;
struct treeNode {
  char data[4];
  tree left,
       right;
};

FILE * prtFile;


tree createTreeNode(
  String pData)
{
  tree lNode;

  lNode = (tree)malloc(sizeof(struct treeNode));
  strncpy(lNode->data, pData, 4);
  lNode->left  = 0 ;
  lNode->right = 0 ;
  return lNode;
}


void process(
  String pData)
{
  fprintf(prtFile, "%s", pData);
}


void preOrder(
  tree pTree)
{
  if (pTree != 0 ) {
    process(pTree->data);
    preOrder(pTree->left);
    preOrder(pTree->right);
  }
}


void inOrder(
  tree pTree)
{
  if (pTree != 0 ) {
    inOrder(pTree->left);
    process(pTree->data);
    inOrder(pTree->right);
  }
}


void postOrder(
  tree pTree)
{
  if (pTree != 0 ) {
    postOrder(pTree->left);
    postOrder(pTree->right);
    process(pTree->data);
  }
}


main()
{
  tree  expTree, t1, t2, t3, t4;

  /* prtFile = fopen("treeTrav.prt", "w"); SF030609 */
  prtFile = stdout; /* SF030609 */
  t1        = createTreeNode(" + ");
  t1->left  = createTreeNode(" a ");
  t1->right = createTreeNode(" b ");
  t2        = createTreeNode(" / ");
  t2->left  = createTreeNode(" d ");
  t2->right = createTreeNode(" e ");
  t3        = createTreeNode(" - ");
  t3->left  = createTreeNode(" c ");
  t3->right = t2;
  expTree        = createTreeNode(" * ");
  expTree->left  = t1;
  expTree->right = t3;
  preOrder (expTree);
  fprintf(prtFile, "\n");
  inOrder  (expTree);
  fprintf(prtFile, "\n");
  postOrder(expTree);
  fprintf(prtFile, "\n");
  return 0;
}
