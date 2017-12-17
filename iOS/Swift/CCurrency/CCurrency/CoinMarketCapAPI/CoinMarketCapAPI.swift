//
//  CoinMarketCapAPI.swift
//  CCurrency
//
//  Created by Utku Yeğen on 16.12.2017.
//  Copyright © 2017 Utku Yeğen. All rights reserved.
//

import Alamofire
import Unbox

class CoinMarketCapAPI {
    public static func ticker(completionHandler: @escaping (_ result: [ModelItem]) -> ()) {
        DispatchQueue.global(qos: .userInitiated).async {
            Alamofire.request("https://api.coinmarketcap.com/v1/ticker/").responseData { (response) in
                if let json = response.result.value {
                    let tickerResult: [ModelItem] = try! unbox(data: json) as [ModelItem]
                    completionHandler(tickerResult)
                }
            }
        }
    }
}
