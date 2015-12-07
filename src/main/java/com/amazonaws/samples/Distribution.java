/*
 * Copyright 2010-2013 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.amazonaws.samples;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * This sample demonstrates how to make basic requests to Amazon S3 using
 * the AWS SDK for Java.
 * <p>
 * <b>Prerequisites:</b> You must have a valid Amazon Web Services developer
 * account, and be signed up to use Amazon S3. For more information on
 * Amazon S3, see http://aws.amazon.com/s3.
 * <p>
 * <b>Important:</b> Be sure to fill in your AWS access credentials in
 * ~/.aws/credentials (C:\Users\USER_NAME\.aws\credentials for Windows
 * users) before you try to run this sample.
 */
public class Distribution {
    private static AmazonS3 s3 = new AmazonS3Client();
    private static Region usWest2;
    private static String VNBucket = "";

    static {
        usWest2 = Region.getRegion(Regions.US_WEST_2);
        s3.setRegion(usWest2);
    }

//    public static void main(String[] args) throws IOException {
//        /*
//         * Create your credentials file at ~/.aws/credentials (C:\Users\USER_NAME\.aws\credentials for Windows users)
//         * and save the following lines after replacing the underlined values with your own.
//         *
//         * [default]
//         * aws_access_key_id = YOUR_ACCESS_KEY_ID
//         * aws_secret_access_key = YOUR_SECRET_ACCESS_KEY
//         */
//
//
//
//
//        "my-first-s3-bucket-" + UUID.randomUUID();
//        String key = "MyObjectKey";
//
//        System.out.println("===========================================");
//        System.out.println("Getting Started with Amazon S3");
//        System.out.println("===========================================\n");
//
//        try {
//            /*
//             * Create a new S3 bucket - Amazon S3 bucket names are globally unique,
//             * so once a bucket name has been taken by any user, you can't create
//             * another bucket with that same name.
//             *
//             * You can optionally specify a location for your bucket if you want to
//             * keep your data closer to your applications or users.
//             */
//            System.out.println("Creating bucket " + bucketName + "\n");
//            s3.createBucket(bucketName);
//
//            /*
//             * List the buckets in your account
//             */
//            System.out.println("Listing buckets");
//            for (Bucket bucket : s3.listBuckets()) {
//                System.out.println(" - " + bucket.getName());
//            }
//            System.out.println();
//
//            /*
//             * Upload an object to your bucket - You can easily upload a file to
//             * S3, or upload directly an InputStream if you know the length of
//             * the data in the stream. You can also specify your own metadata
//             * when uploading to S3, which allows you set a variety of options
//             * like content-type and content-encoding, plus additional metadata
//             * specific to your applications.
//             */
//            System.out.println("Uploading a new object to S3 from a file\n");
//            s3.putObject(new PutObjectRequest(bucketName, key, createSampleFile()));
//
//            /*
//             * Download an object - When you download an object, you get all of
//             * the object's metadata and a stream from which to read the contents.
//             * It's important to read the contents of the stream as quickly as
//             * possibly since the data is streamed directly from Amazon S3 and your
//             * network connection will remain open until you read all the data or
//             * close the input stream.
//             *
//             * GetObjectRequest also supports several other options, including
//             * conditional downloading of objects based on modification times,
//             * ETags, and selectively downloading a range of an object.
//             */
//            System.out.println("Downloading an object");
//            S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
//            System.out.println("Content-Type: "  + object.getObjectMetadata().getContentType());
//            displayTextInputStream(object.getObjectContent());
//
//            /*
//             * List objects in your bucket by prefix - There are many options for
//             * listing the objects in your bucket.  Keep in mind that buckets with
//             * many objects might truncate their results when listing their objects,
//             * so be sure to check if the returned object listing is truncated, and
//             * use the AmazonS3.listNextBatchOfObjects(...) operation to retrieve
//             * additional results.
//             */
//            System.out.println("Listing objects");
//            ObjectListing objectListing = s3.listObjects(new ListObjectsRequest()
//                    .withBucketName(bucketName)
//                    .withPrefix("My"));
//            for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
//                System.out.println(" - " + objectSummary.getKey() + "  " +
//                        "(size = " + objectSummary.getSize() + ")");
//            }
//            System.out.println();
//
//            /*
//             * Delete an object - Unless versioning has been turned on for your bucket,
//             * there is no way to undelete an object, so use caution when deleting objects.
//             */
//            System.out.println("Deleting an object\n");
//            s3.deleteObject(bucketName, key);
//
//            /*
//             * Delete a bucket - A bucket must be completely empty before it can be
//             * deleted, so remember to delete any objects from your buckets before
//             * you try to delete them.
//             */
//            System.out.println("Deleting bucket " + bucketName + "\n");
//            s3.deleteBucket(bucketName);
//        } catch (AmazonServiceException ase) {
//            System.out.println("Caught an AmazonServiceException, which means your request made it "
//                    + "to Amazon S3, but was rejected with an error response for some reason.");
//            System.out.println("Error Message:    " + ase.getMessage());
//            System.out.println("HTTP Status Code: " + ase.getStatusCode());
//            System.out.println("AWS Error Code:   " + ase.getErrorCode());
//            System.out.println("Error Type:       " + ase.getErrorType());
//            System.out.println("Request ID:       " + ase.getRequestId());
//        } catch (AmazonClientException ace) {
//            System.out.println("Caught an AmazonClientException, which means the client encountered "
//                    + "a serious internal problem while trying to communicate with S3, "
//                    + "such as not being able to access the network.");
//            System.out.println("Error Message: " + ace.getMessage());
//        }
//    }
//
//    /**
//     * Creates a temporary file with text data to demonstrate uploading a file
//     * to Amazon S3
//     *
//     * @return A newly created temporary file with text data.
//     *
//     * @throws IOException
//     */
//    private static File createSampleFile() throws IOException {
//        File file = File.createTempFile("aws-java-sdk-", ".txt");
//        file.deleteOnExit();
//
//        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
//        writer.write("abcdefghijklmnopqrstuvwxyz\n");
//        writer.write("01234567890112345678901234\n");
//        writer.write("!@#$%^&*()-=[]{};':',.<>/?\n");
//        writer.write("01234567890112345678901234\n");
//        writer.write("abcdefghijklmnopqrstuvwxyz\n");
//        writer.close();
//
//        return file;
//    }
//
//    /**
//     * Displays the contents of the specified input stream as text.
//     *
//     * @param input
//     *            The input stream to display as text.
//     *
//     * @throws IOException
//     */
//    private static void displayTextInputStream(InputStream input) throws IOException {
//        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
//        while (true) {
//            String line = reader.readLine();
//            if (line == null) break;
//
//            System.out.println("    " + line);
//        }
//        System.out.println();
//    }

    public static void upload(File VNgame, String imagUrl, String author, String gameName) {

        ObjectListing objectListing = s3.listObjects(new ListObjectsRequest()
            .withBucketName(VNBucket));
        int size = objectListing.getObjectSummaries().size();


        ObjectMetadata metadata = new ObjectMetadata();
        metadata.addUserMetadata("imagUrl", imagUrl);
        metadata.addUserMetadata("author", author);
        metadata.addUserMetadata("gameName", gameName);
        s3.putObject(new PutObjectRequest(VNBucket, size + "", VNgame).withMetadata(metadata));
    }

    public static boolean download(String path, String key) {
        InputStream inputStream = s3.getObject(VNBucket, key).getObjectContent();

        OutputStream outputStream = null;

        try {
            // read this file into InputStream

            // write the inputStream to a FileOutputStream
            outputStream =
                new FileOutputStream(new File(path));

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

            System.out.println("Done!");

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            if (outputStream != null) {
                try {
                    // outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }

            }
        }
        return true;
    }


    public static ArrayList<Map<String,String>> generateList() {
        ObjectListing objectListing = s3.listObjects(new ListObjectsRequest()
            .withBucketName(VNBucket));
        ArrayList<Map<String,String>> summary = new ArrayList<Map<String,String>>();
        int size = objectListing.getObjectSummaries().size();
        for (int i = 0; i < size; ++i) {
            summary.add(s3.getObject(VNBucket, i + "").getObjectMetadata().getUserMetadata());
        }
        return summary;
    }


}
