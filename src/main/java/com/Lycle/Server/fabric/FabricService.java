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
import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;

@Service
@Getter
@Slf4j
public class FabricService{

    Wallet wallet;
    byte[] queryAllResult = new byte[0];

    // Path to a common connection profile describing the network.
    Path walletDirectory = Paths.get("/home/lycle.sungshin/server/wallet");
    Path networkConfigFile = Paths.get("/home/lycle.sungshin/server/connection.json");

    public byte[] getReward(String email) throws IOException {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");

        wallet = Wallet.createFileSystemWallet(walletDirectory);

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
            queryAllResult = contract.evaluateTransaction("getPoint", String.valueOf(info));
            log.info(String.valueOf(LocalDateTime.now()));

        } catch (ContractException | JSONException e) {
            e.printStackTrace();
        }

        builder.connect().close();
        log.info(String.valueOf(LocalDateTime.now()));
        return queryAllResult;
    }

    public byte[] registerUser(String email) throws IOException{
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");

        // Load an existing wallet holding identities used to access the network.
        wallet = Wallet.createFileSystemWallet(walletDirectory);

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
            queryAllResult = contract.submitTransaction("registerUser",String.valueOf(info));
            log.info(String.valueOf(LocalDateTime.now()));

        } catch (ContractException | InterruptedException | TimeoutException | JSONException e) {
            e.printStackTrace();
        }
        builder.connect().close();
        log.info(String.valueOf(LocalDateTime.now()));
        return queryAllResult;

    }

    public byte[] depositReward(String email, int point) throws IOException {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");

        wallet = Wallet.createFileSystemWallet(walletDirectory);

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
            info.put("Point", point);

            // Evaluate transactions that query state from the ledger.
            queryAllResult = contract.submitTransaction("depositPoint", String.valueOf(info));

        } catch (ContractException | JSONException | InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        builder.connect().close();
        log.info(String.valueOf(LocalDateTime.now()));
        return queryAllResult;
    }

    public byte[] exchangeReward(String senderEmail, String receiverEmail, int point) throws IOException{
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "false");

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
            queryAllResult = contract.submitTransaction("transferPoint", String.valueOf(info));


        } catch (ContractException | InterruptedException | TimeoutException | JSONException e) {
            e.printStackTrace();
        }
        builder.connect().close();
        log.info(String.valueOf(LocalDateTime.now()));
        return queryAllResult;

    }


}
