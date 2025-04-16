#include <iostream>
using namespace std;

void fillLine(int n = 25, char c = '*') { // n개의c 문자를한라인에출력
	for (int i = 0; i < n; i++) cout << c;
	cout << endl;
}

int main() {
	fillLine(); // 25개의'*'를한라인에출력
	fillLine(10, '%'); // 10개의'%'를한라인에출력
}