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
    
    let arr = ["1", "2", "3"]
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        list.dataSource = self
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.arr.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "Row", for: indexPath) as! ListRow
        
        cell.rank.text = arr[indexPath.item]
        
        return cell
    }
}

