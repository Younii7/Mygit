#include <iostream>
using namespace std;

int main() {
	cout << "i" << '\t' << "n" << '\t' << "refn" << endl;
	cout << "i와 n의 값을 입력하시오 : ";
	int i,n;
	cin >> i >> n;
	int& refn = n; // 참조변수refn선언. refn은n에대한별명
	n = 4;
	refn++; // refn=5, n=5
	cout << i << '\t' << n << '\t' << refn << endl;

	refn = i; // refn=1, n=1
	refn++; // refn=2, n=2
	cout << i << '\t' << n << '\t' << refn << endl;

	int* p = &refn; // p는n의주소를가짐
	*p = 20; // refn=20, n=20
	cout << i << '\t' << n << '\t' << refn << endl;
}