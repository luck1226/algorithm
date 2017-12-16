#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
#include <fstream>
#include <string>
#include <cstdlib>

using namespace std;

struct Book {
	int order_id;
	char side;
	double price;
	int size;
	int level;
};

struct largePrice {
	inline bool operator() (const Book b1, const Book b2) {
		return (b1.price <  b2.price);
	}
};

struct lessPrice {
        inline bool operator() (const Book b1, const Book b2) {
                return (b1.price > b2.price);
        }
};

class OrderBook {
	private:
		vector<Book> Bk;
	public:
		void add(int order_id, char side, double price, int size);
		void modify(int order_id, int new_size);
		void remove(int order_id);
		double get_price(char side, int level);
		int get_size(char side, int level);
};

void OrderBook::add(int order_id, char side, double price, int size) {

	struct Book b;
	b.order_id = order_id;
	b.side = side;
	b.price = price;
	b.size = size;

	Bk.push_back(b);
}

void OrderBook::modify(int order_id, int new_size) {

	int len = Bk.size();
	bool go = true;
	int i = 0;
	while (go && i < len) {
		if (Bk[i].order_id == order_id) { go = false; }
		else { i++; }
	}
	if (i < len) { Bk[i].size = new_size; }

}

void OrderBook::remove(int order_id) {

        int len = Bk.size();
        bool go = true;
        int i = 0;
        while (go && i < len) {
                if (Bk[i].order_id == order_id) { go = false; }
                else { i++; }
        }
        
	if (i < len) { Bk.erase(Bk.begin()+i); }

}

double OrderBook::get_price(char side, int level) {
	vector<Book> tmp;
	int len = Bk.size();
	int i = 0;

	while (i < len) {
		if (Bk[i].side == side) {
			tmp.push_back(Bk[i]);
		}
		i++;
	}
	
	if (side == 'S') {
		sort(tmp.begin(), tmp.end(), largePrice());
		double tmpP = 0;
		int i = level-1;
		int ll = tmp.size();
		while ( i < ll ) {
			if (tmp[i].price == tmp[level-1].price) { tmpP+=tmp[i].price; }
			i++;
		}
		return tmpP;
	}
	else {
		sort(tmp.begin(), tmp.end(), lessPrice());
		double tmpP = 0;
		int i = level-1;
		int ll = tmp.size();
		while ( i < ll ) {
			if (tmp[i].price == tmp[level-1].price) { tmpP+=tmp[i].price; }
			i++;
		}
		return tmpP;
	}
	
}

int OrderBook::get_size(char side, int level) {
        vector<Book> tmp;
        int len = Bk.size();
        int i = 0;

        while (i < len) {
                if (Bk[i].side == side) {
                        tmp.push_back(Bk[i]);
                }
		i++;
        }

        if (side == 'S') {
                sort(tmp.begin(), tmp.end(), largePrice());
		int pp = 0.0;
		int i = level-1;
		int ll = tmp.size();
		while (i<ll) {
			if (tmp[level-1].price == tmp[i].price) { pp+=tmp[i].size; }
			i++;
		}
		return pp;
        }
        else {
                sort(tmp.begin(), tmp.end(), lessPrice());
		int pp = 0.0;
                int i = level-1;
                int ll = tmp.size();
                while (i<ll) {
                        if (tmp[level-1].price == tmp[i].price) { pp+=tmp[i].size; }
                        i++;
                }
                return pp;
        }

}

int main() {
	
	OrderBook ob;
	
	ifstream infile("input.txt");

	while (infile) {
		
		string strInput;
		infile >> strInput;

		if (strInput == "add") { 
			int id;
			char side;
			double price;
			int size;
			infile >> id >> side >> price >> size;
			ob.add(id, side, price, size);			
		}
		else if (strInput == "modify") {
			int id;
			int size;
			infile >> id >> size;
			ob.modify(id, size);
		}
		else if (strInput == "remove") {
			int id;
			infile >> id;
			ob.remove(id);
		}
		else if (strInput == "get") {
			string comment;
			infile >> comment;
			if (comment == "size") {
				char side;
				int level;
				infile >> side >> level;
				cout << ob.get_size(side, level) << endl;
			}
			else {
				char side;
				int level;
				infile >> side >> level;
				cout << ob.get_price(side, level) << endl;
			}
		}
	}
	return 0;

}
