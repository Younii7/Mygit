#include <iostream>
using namespace std;

class Power {
	int kick;
	int punch;
public:
	Power(int kick = 0, int punch = 0) {
		this->kick = kick; this->punch = punch;
	}
	void show();
	Power operator+ (Power op2); // + 연산자 함수 선언
	Power operator- (Power op2); // + 연산자 함수 선언

};

void Power::show() {
	cout << "kick=" << kick << ',' << "punch=" << punch << endl;
}

Power Power::operator+(Power op2) {
	Power tmp; // 임시 객체 생성
	tmp.kick = this->kick + op2.kick; // kick 더하기
	tmp.punch = this->punch + op2.punch; // punch 더하기
	return tmp; // 더한 결과 리턴
}

Power Power::operator-(Power op2) {
	Power tmp; // 임시 객체 생성
	tmp.kick = this->kick - op2.kick; // kick 더하기
	tmp.punch = this->punch - op2.punch; // punch 더하기
	return tmp; // 더한 결과 리턴
}

int main() {
	int ak, ap, bk, bp;
	cout << "a의 kick과 power의 값을 입력하시오 >> ";
	cin >> ak >> ap;
	cout << "b의 kick과 power의 값을 입력하시오 >> ";
	cin >> bk >> bp;
	Power a(ak, ap), b(bk, bp), c, d;
	c = a + b; // 파워 객체 + 연산
	d = a - b;
	a.show();
	b.show();
	c.show();
	d.show();
}