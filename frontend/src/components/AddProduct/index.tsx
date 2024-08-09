import React from "react";
import * as Yup from "yup";
import { SubmitHandler, useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { clsx } from "@/utils/string";
import scss from "./addproduct.module.scss";
import { useRouter } from "next/router";
import { Button, FileInput, TextInput } from "@mantine/core";
import { FaCamera, FaUpload } from "react-icons/fa";

type DashboardData = {
  pName: string;
  pDescription: string;
  price: string;
  offerPrice: string;
  offerPercentage: string;
  size: string;
  color: string;
  image1: File | undefined | any;
  image2: File | undefined | any;
  image3: File | undefined | any;
  image4: File | undefined | any;
  image5: File | undefined | any;
  quantityBySize: string;
};

const validationSchema = Yup.object({
  pname: Yup.string().required("Please Enter Product Name"),
  pdescription: Yup.string().required("Please Enter Product Description"),
  price: Yup.string().required("Please Enter Product Price"),
  offerprice: Yup.string().required("Please Enter Offer Price"),
  size: Yup.string().required("Please Enter Product Size"),
  color: Yup.string().required("Please Enter Product Color"),
  qty: Yup.string().required("Please Enter Product Quentity"),
  offerpercentage: Yup.string().required("Please Enter percentage"),
  image1: Yup.mixed().required("File is required"),
  image2: Yup.mixed().required("File is required"),
  image3: Yup.mixed().required("File is required"),
  image4: Yup.mixed().optional(),
  image5: Yup.mixed().optional(),
});
const AddProduct = () => {
  const router = useRouter();
  const {
    register,
    handleSubmit,
    setValue,
    clearErrors,
    formState: { errors },
  } = useForm<DashboardData>({
    resolver: yupResolver(validationSchema),
    mode: "onChange",
  });

  const onSubmit: SubmitHandler<DashboardData> = async (data) => {
    console.log("first", data);
  };

  return (
    <div className={scss.maindiv}>
      <div className={clsx(scss.present_adress, "container")}>
        <form noValidate onSubmit={handleSubmit(onSubmit)}>
          <div className={scss.presentaddressheading}>
            <h2>Add Product</h2>
          </div>
          <div className={scss.presentadress_content}>
            <div className="row">
              <div className="col-md-4 col-sm-12">
                <TextInput
                  withAsterisk
                  label="Product Name"
                  {...register("pName")}
                  error={errors.pName?.message}
                />
              </div>
              <div className="col-md-4 col-sm-12">
                <TextInput
                  withAsterisk
                  label="Product Description"
                  {...register("pDescription")}
                  error={errors.pDescription?.message}
                />
              </div>
              <div className="col-md-4 col-sm-12">
                <TextInput
                  withAsterisk
                  label="Product Price"
                  {...register("price")}
                  error={errors.price?.message}
                />
              </div>
            </div>

            <div className="row">
              <div className="col-md-4 col-sm-12">
                <TextInput
                  withAsterisk
                  label="Product Offer Price"
                  {...register("offerPrice")}
                  error={errors.offerPrice?.message}
                />
              </div>
              <div className="col-md-4 col-sm-12">
                <TextInput
                  withAsterisk
                  label="Product Offer Percentage"
                  {...register("offerPercentage")}
                  error={errors.offerPercentage?.message}
                />
              </div>
              <div className="col-md-4 col-sm-12">
                <TextInput
                  withAsterisk
                  label="Product Size"
                  {...register("size")}
                  error={errors.size?.message}
                />
              </div>
            </div>

            <div className="row">
              <div className="col-md-4 col-sm-12">
                <TextInput
                  withAsterisk
                  label="Product Color"
                  {...register("color")}
                  error={errors.color?.message}
                />
              </div>
              <div className="col-md-4 col-sm-12">
                <TextInput
                  withAsterisk
                  label="Product Quantity"
                  {...register("quantityBySize")}
                  error={errors.quantityBySize?.message}
                />
              </div>
            </div>

            <div className="row">
              <div className="col-md-4 col-sm-12">
                <FileInput
                  rightSection={<FaUpload />}
                  withAsterisk
                  label="Product Image1"
                  onChange={(value: any) => {
                    setValue("image1", value);
                    clearErrors("image1");
                  }}
                  error={errors.image1?.message}
                />
              </div>
              <div className="col-md-4 col-sm-12">
                <FileInput
                  rightSection={<FaUpload />}
                  withAsterisk
                  label="Product Image2"
                  onChange={(value: any) => {
                    setValue("image2", value);
                    clearErrors("image2");
                  }}
                  error={errors.image2?.message}
                />
              </div>
              <div className="col-md-4 col-sm-12">
                <FileInput
                  rightSection={<FaUpload />}
                  withAsterisk
                  label="Product Image3"
                  onChange={(value: any) => {
                    setValue("image3", value);
                    clearErrors("image3");
                  }}
                  error={errors.image3?.message}
                />
              </div>
            </div>

            <div className="row">
              <div className="col-md-4 col-sm-12">
                <FileInput
                  rightSection={<FaUpload />}
                  withAsterisk
                  label="Product Image4"
                  onChange={(value: any) => {
                    setValue("image4", value);
                    clearErrors("image4");
                  }}
                  error={errors.image4?.message}
                />
              </div>
              <div className="col-md-4 col-sm-12">
                <FileInput
                  rightSection={<FaUpload />}
                  withAsterisk
                  label="Product Image5"
                  onChange={(value: any) => {
                    setValue("image5", value);
                    clearErrors("image5");
                  }}
                  error={errors.image5?.message}
                />
              </div>
            </div>

            <div className="d-flex justify-content-center align-items-center mt-4">
              <Button
                w={200}
                style={{ backgroundColor: "#05395c" }}
                className={scss.submitbtn}
                type="submit"
                // disabled={new Date() > new Date("editDeactiveDate")}
              >
                Add Product
              </Button>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AddProduct;
