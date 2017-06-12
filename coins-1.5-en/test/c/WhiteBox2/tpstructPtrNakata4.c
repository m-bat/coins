/* tpstructPtrNakata4.c:  struct and pointer */
/*   Nakata mail 040213 */
/* Display representation in nested function for PL0 */

int printf(char*, ...);

void *display_0; /* added */
void *display_1;
void *display_2;

struct fgh_frame{
    int w; int u;
};
struct abc_frame{
    int z; int a; int b;
};
struct main_frame{
    int x; int y;
};

int fgh(int w){
  void *saveDisplay_;
  struct fgh_frame frame_ = {1, 2}; 
  int ret_val_var;
  saveDisplay_ = display_2;
  display_2 = &frame_;
  frame_.w = w;
  frame_.u = (((struct main_frame *)display_0) -> x) 
            + (((struct abc_frame *)display_1) -> a)
            + (((struct abc_frame *)display_1) -> z);
  ret_val_var = frame_.u + frame_.w;
  display_2 = saveDisplay_;
  printf(" fgh= %d\n", ret_val_var);  /***/
  return ret_val_var;
  display_2 = saveDisplay_;
  return 0;
}

int abc(int z){
  void *saveDisplay_;
  struct abc_frame frame_ = {10, 20, 30}; 
  int ret_val_var;
  saveDisplay_ = display_1;
  display_1 = &frame_;
  frame_.z = z;
  frame_.a = frame_.z + (((struct main_frame *)display_0) -> x);
  ret_val_var = fgh(frame_.a);
  display_1 = saveDisplay_;
  printf(" abc= %d\n", ret_val_var);  /***/
  return ret_val_var;
  display_1 = saveDisplay_;
  return 0; 
}

main(){
  void *saveDisplay_;
  struct main_frame frame_ = {100, 200};
  saveDisplay_ = display_0;
  display_0 = &frame_;
  frame_.x = abc(frame_.y);
  printf(" main x= %d\n", frame_.x);  /***/
  display_0 = saveDisplay_;
  return 0;
}
