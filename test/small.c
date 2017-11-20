int vb, a = 2;
int somefunc();
int somefunc2(int, double);
int somefunc3(double);
int main(){
    int a[];
    a[0] = 2;
    a[1] = 3;
    return somefunc2(a[0],a[1]);
}
int somefunc(){
    return 78;
}