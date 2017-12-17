
//int algo(int*);
int main(){
  int a,b;
  printf("Ingrese un numero: ");
  scanf("%d",&a);
  while(a > 0){
    printf("a=%d\n", a);
    a--;
  }
  algo(&a,b,b);
  valor(a,b,a);
  for(b=0; b<3; b++){
    {
      char x;
      x = 3 + 4 + 'b' * 5 + b;
    }
    {
      int* x;
      x=&a;
    }
    printf("for_a=%d\n",a);
  }
  if (a == 3 && b == 2 ) {
    printf("Just as expected\n");
  }else{
    printf("WHAT THE HELL\n");
  }
  printf("a=%d\n", a);
  printf("b=%d\n", b);
  debug();
  return 0;
}
void debug(){
  printf("debugging\n");
}
int algo(int* x, int y, int z){
  *x=3;
  y = z;
  printf("x=%d",x);
  printf("*x=%d",*x);
  return 0;
}
int valor(int x, int y, int z){
  int h,a,r;
  h=2;
  a=3;
  x=h*a;
  r= h+x*(h+a);
  return 0;
}
