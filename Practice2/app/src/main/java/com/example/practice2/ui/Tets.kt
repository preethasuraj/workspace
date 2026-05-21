fun maxProfit(prices: IntArray): Int {
    var maxProfit = 0
    for (i in prices.indices - 1) {
        maxProfit = maxProfit + maxOf(
            prices[i + 1] - prices[i], 0
        )

    }

}