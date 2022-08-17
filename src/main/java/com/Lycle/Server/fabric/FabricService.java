package com.Lycle.Server.fabric;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.gateway.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeoutException;

@Service
@Getter
@Slf4j
public class FabricService{

    // Path to a common connection profile describing the network.
    Path walletDirectory = Paths.get("/home/lycle.sungshin/server/wallet");
    Path networkConfigFile = Paths.get("/home/lycle.sungshin/server/connection.json");

    public byte[] getReward(String email) throws IOException {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "false");

        byte[] queryAllRewardsResult = new byte[0];

        // Load an existing wallet holding identities used to access the network.
        Wallet wallet = Wallet.createFileSystemWallet(walletDirectory);

        // Configure the gateway connection used to access the network.
        Gateway.Builder builder = Gateway.createBuilder()
                .identity(wallet, "User1")
                .networkConfig(networkConfigFile)
                .discovery(true);

        // Create a gateway connection
        try (Gateway gateway = builder.connect()) {

            // Obtain a smart contract deployed on the network.
            Network network = gateway.getNetwork("reward");
            Contract contract = network.getContract("lycle");

            JSONObject info = new JSONObject();

            info.put("Email",email);

            // Evaluate transactions that query state from the ledger.
            queryAllRewardsResult = contract.evaluateTransaction("getPoint", String.valueOf(info));


        } catch (ContractException | JSONException e) {
            e.printStackTrace();
        }

        return queryAllRewardsResult;
    }

    public byte[] registerUser(String email) throws IOException{
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "false");

        byte[] queryUserInfoResult = new byte[0];

        // Load an existing wallet holding identities used to access the network.
        Wallet wallet = Wallet.createFileSystemWallet(walletDirectory);

        // Configure the gateway connection used to access the network.
        Gateway.Builder builder = Gateway.createBuilder()
                .identity(wallet, "User1")
                .networkConfig(networkConfigFile)
                .discovery(true);

        // Create a gateway connection
        try (Gateway gateway = builder.connect()) {

            // Obtain a smart contract deployed on the network.
            Network network = gateway.getNetwork("reward");
            Contract contract = network.getContract("lycle");

            JSONObject info = new JSONObject();
            info.put("Email", email);

            // Evaluate transactions that query state from the ledger.
            queryUserInfoResult = contract.submitTransaction("registerUser",String.valueOf(info));


        } catch (ContractException | InterruptedException | TimeoutException | JSONException e) {
            e.printStackTrace();
        }

        return queryUserInfoResult;

    }

    public byte[] exchangeReward(String senderEmail, String receiverEmail, int point) throws IOException{
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "false");

        byte[] queryRewardResult = new byte[0];

        // Load an existing wallet holding identities used to access the network.
        Wallet wallet = Wallet.createFileSystemWallet(walletDirectory);

        // Configure the gateway connection used to access the network.
        Gateway.Builder builder = Gateway.createBuilder()
                .identity(wallet, "User1")
                .networkConfig(networkConfigFile)
                .discovery(true);

        // Create a gateway connection
        try (Gateway gateway = builder.connect()) {

            // Obtain a smart contract deployed on the network.
            Network network = gateway.getNetwork("reward");
            Contract contract = network.getContract("lycle");

            JSONObject info = new JSONObject();

            info.put("Sender", senderEmail);
            info.put("Recipient", receiverEmail);
            info.put("Point",point);

            // Evaluate transactions that query state from the ledger.
            queryRewardResult = contract.submitTransaction("transferPoint", String.valueOf(info));


        } catch (ContractException | InterruptedException | TimeoutException | JSONException e) {
            e.printStackTrace();
        }

        return queryRewardResult;

    }

}
