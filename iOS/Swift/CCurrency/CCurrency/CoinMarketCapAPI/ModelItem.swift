//
//  ModelItem.swift
//  CCurrency
//
//  Created by Utku Yeğen on 16.12.2017.
//  Copyright © 2017 Utku Yeğen. All rights reserved.
//

import Unbox

struct ModelItem: Unboxable {
    let id: String
    let name: String
    let symbol: String
    let rank: String
    let price_usd: String
    let price_btc: String
    let volume_usd_24h: String
    let market_cap_usd: String
    let available_supply: String
    let total_supply: String
    var max_supply: String
    let percent_change_1h: String
    let percent_change_24h: String
    let percent_change_7d: String
    let last_updated: String
    
    init(unboxer: Unboxer) throws {
        self.id = try unboxer.unbox(key: "id")
        self.name = try unboxer.unbox(key: "name")
        self.symbol = try unboxer.unbox(key: "symbol")
        self.rank = try unboxer.unbox(key: "rank")
        self.price_usd = try unboxer.unbox(key: "price_usd")
        self.price_btc = try unboxer.unbox(key: "price_btc")
        self.volume_usd_24h = try unboxer.unbox(key: "24h_volume_usd")
        self.market_cap_usd = try unboxer.unbox(key: "market_cap_usd")
        self.available_supply = try unboxer.unbox(key: "available_supply")
        self.total_supply = try unboxer.unbox(key: "total_supply")
        do {
            self.max_supply = try unboxer.unbox(key: "max_supply")
        } catch {
            self.max_supply = ""
        }
        self.percent_change_1h = try unboxer.unbox(key: "percent_change_1h")
        self.percent_change_24h = try unboxer.unbox(key: "percent_change_24h")
        self.percent_change_7d = try unboxer.unbox(key: "percent_change_7d")
        self.last_updated = try unboxer.unbox(key: "last_updated")
    }
}
