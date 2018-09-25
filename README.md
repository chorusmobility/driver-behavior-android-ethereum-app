# Chorus Mobility - Good Driving Behaviour Android Demo

Chorus Mobility developed a prototype to test the feasibility of the V2X platform solution. We decided on a use case where car insurance companies can incentivize good driving behaviour of their clients via our platform. Good driving behaviour comprises several factors such as: Keeping the distance to the car in front, no aggressive acceleration, no excessive speeding, no hard breaking if not necessary, or not using the phone while driving. Not only do these good driving behaviour guidelines increase the overall safety of the driver as well as all other road users, they also help to mitigate traffic congestions in urban areas. The economic incentive concept for the Chorus prototype is rather simple. Based on the driving behaviour score for each ride as well as the drivers' overall score, a token reward is calculated by the insurance company. The better the driving behaviour, the higher the reward since good driving behaviour usually corresponds to less claims for the insurance company. The token reward is calculated in a pre-defined time interval, e.g., after each ride, each week, or each month, and the reward tokens are transferred to the user. In case the user behaves badly, a token penalty has to be paid. The users can later use their token to claim cash-backs from the insurance company, to negotiate a lower price for next years insurance, or exchange them for goods and services depending on the insurer.
           
The latest version of the prototype uses our ERC-20 compliant [COR token](https://github.com/chorusmobility/chorus-token-generation-ethereum-contract) on the Rinkeby TestNet for demonstration purposes. However, later version may rely on the [ERC-721](https://github.com/ethereum/EIPs/blob/master/EIPS/eip-721.md) standard for non-fungible tokens. The idea is to let the insurers hand out ERC-721 tokens to their customers as rewards and burn them when the users trade the accumulated rewards for price reductions or similar.


The Android app implements the blockchain smart contract integration and business logic allowing to make P2P micro payments and providing driver behaviour analytics.


# Setup Guide and Technical Background

Communication with blockchain and IPFS on Android implemented with using INFURA and Web3j.


TODO



# License

All Rights Reserved. Proof-of-Stake Inc.
