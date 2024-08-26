import {Category} from "../../categories/models/categories.models";
import {PaymentAccount} from "../../accounts/models/accounts.models";

export interface Transaction {
  id?: number
  ownerId: number
  description: string
  currency: string
  amount: number
  transactionDate: Date
  type: string
  paymentAccountId: number
  categoryId: number
  displayedAmount?: string
  category?: Category
  paymentAccount?: PaymentAccount
}
