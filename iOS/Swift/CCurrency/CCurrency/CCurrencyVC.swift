//
//  ViewController.swift
//  CCurrency
//
//  Created by Utku Yeğen on 8.12.2017.
//  Copyright © 2017 Utku Yeğen. All rights reserved.
//

import UIKit

class CCurrencyVC: UIViewController, UITableViewDataSource {
    
    @IBOutlet weak var list: UITableView!
    
    var currList = [ModelItem]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        list.dataSource = self
        
        CoinMarketCapAPI.ticker { (result) in
            self.currList = result
            self.list.reloadData()
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.currList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "Row", for: indexPath) as! ListRow
        
        cell.rank.text = self.currList[indexPath.item].rank
        cell.symbol.text = self.currList[indexPath.item].symbol
        cell.name.text = self.currList[indexPath.item].name
        cell.btcPrize.text = self.currList[indexPath.item].price_btc
        cell.usdPrize.text = self.currList[indexPath.item].price_usd
        
        cell.name.adjustsFontSizeToFitWidth = true
        
        return cell
    }
}

