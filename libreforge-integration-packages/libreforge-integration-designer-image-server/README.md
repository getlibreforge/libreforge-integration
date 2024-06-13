**_Documentation_**

**Overview**

This project uses an [ImageService.java](src%2Fmain%2Fjava%2Fcom%2Flibreforge%2Fintegration%2Fimages%2Fapi%2Fservice%2FImageService.java) interface to abstract the implementation details of interacting with image storage services. This 
allows for easy switching between different implementations, such as AWS S3, a mock service for testing, or another cloud provider.

**Steps to Switch Implementations**

1. Define the Interface: The [ImageService.java](src%2Fmain%2Fjava%2Fcom%2Flibreforge%2Fintegration%2Fimages%2Fapi%2Fservice%2FImageService.java) interface defines 
the contract for storing operations.

2. Implement the Interface: Create a class that implements the [ImageService.java](src%2Fmain%2Fjava%2Fcom%2Flibreforge%2Fintegration%2Fimages%2Fapi%2Fservice%2FImageService.java) interface. The current implementation is [AwsS3ServiceImpl.java](src%2Fmain%2Fjava%2Fcom%2Flibreforge%2Fintegration%2Fimages%2Fapi%2Fservice%2FAwsS3ServiceImpl.java).

3. Update the Controller: The ImagesController uses the [ImageService.java](src%2Fmain%2Fjava%2Fcom%2Flibreforge%2Fintegration%2Fimages%2Fapi%2Fservice%2FImageService.java) interface, ensuring it is not tied to a specific implementation.

4. Configure Dependency Injection: In the ImagesConfig class, specify which implementation of [ImageService.java](src%2Fmain%2Fjava%2Fcom%2Flibreforge%2Fintegration%2Fimages%2Fapi%2Fservice%2FImageService.java) should be used. This can be done by defining a bean for the desired implementation.

5. Switch Implementation: To switch to a different implementation, create a new class that implements S3Service
[ImageService.java](src%2Fmain%2Fjava%2Fcom%2Flibreforge%2Fintegration%2Fimages%2Fapi%2Fservice%2FImageService.java) and update the configuration to inject the new implementation.

**Example: Switching to a Mock Implementation**

1. Create a new class MockS3ServiceImpl that implements S3Service.

2. Update the ImagesConfig class to inject MockS3ServiceImpl instead of AwsS3ServiceImpl.

**CURL requests**

**Upload image**
```
curl -X POST "http://localhost:8080/api/designer/image-server/images" \
-H "Content-Type: multipart/form-data" \
-F "image=@img.png" \
-F "tags=Tag_1" \
-F "tags=Tag_2"
```

**Get images**
```
curl -X POST "http://localhost:8080/api/designer/image-server/images/search" \
-H "Content-Type: application/json" \
-d '{"tags": ["Tag_1"]}'
```

**Delete image**
```
curl -X DELETE "http://localhost:8080/api/designer/image-server/images/ec72c92d-25c6-42d7-b4c4-8ce7a0069f61" \
-H "accept: application/json" \
-H "Content-Type: application/json"
```