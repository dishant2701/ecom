import React from "react";
import scss from "./register.module.scss";
import * as Yup from "yup";
import { SubmitHandler, useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import {
  Box,
  Button,
  LoadingOverlay,
  Paper,
  PasswordInput,
  TextInput,
} from "@mantine/core";
import { FaRegUser } from "react-icons/fa";
import { RiLockPasswordLine } from "react-icons/ri";
import { HiArrowRight } from "react-icons/hi";
import Link from "next/link";
import { FiExternalLink } from "react-icons/fi";
import { MdOutlineMailOutline } from "react-icons/md";
import fetcher from "@/utils/fetcher";
import { toast } from "react-toastify";
import { useRouter } from "next/router";

type RegisterData = {
  name: string;
  username: string;
  // roleId: string;
  email: string;
  password: string;
  confirmPassword: string;
};

const validationSchema = Yup.object({
  name: Yup.string()
    .required("Please Enter Candidate Name")
    .matches(/^[A-Za-z0-9 ]{3,20}$/, {
      message: "Must be 3-20 characters, with only letters / digits ",
    }),
  username: Yup.string()
    .required("Please Enter Mobile Number")
    .matches(/^[0-9]{10}$/, "Mobile number must be 10 digits"),
  email: Yup.string()
    .email("Invalid email address")
    .required("Email is required")
    .matches(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z0-9.]+$/, {
      message: "Enter a valid email address",
    }),
  password: Yup.string()
    .required("Password is required")
    .min(8, "Password must be at least 8 characters")
    .test(
      "uppercase",
      "Password must contain at least one uppercase letter",
      (value) => /[A-Z]/.test(value)
    )
    .test(
      "lowercase",
      "Password must contain at least one lowercase letter",
      (value) => /[a-z]/.test(value)
    )
    .test("number", "Password must contain at least one number", (value) =>
      /\d/.test(value)
    )
    .test(
      "special",
      "Password must contain at least one special character",
      (value) => /[!@#$%^&*(),.?":{}|<>]/.test(value)
    ),
  confirmPassword: Yup.string()
    .required("Please retype your password.")
    .oneOf([Yup.ref("password")], "Your passwords do not match."),
});

const RagisterPage = () => {
  const router = useRouter();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<RegisterData>({
    resolver: yupResolver(validationSchema),
    mode: "onChange",
  });

  const onSubmit: SubmitHandler<RegisterData> = async (data) => {
    try {
      const { confirmPassword, ...json } = data;
      const response = await fetcher("/auth/register", "POST", {
        ...json,
        roleId: 2,
      });
      if (response == "Registered!") {
        toast.success("User Registered successfully");
        router.push("/login");
      }
    } catch (error: any) {
      toast.error(error.message);
    }
  };

  return (
    <div className={scss.container}>
      <Paper withBorder shadow="md" p="lg" className={scss.gridWrap}>
        <div className="p-8 flex items-center justify-center max-[856px]:hidden border-r-2 border-slate-200">
          <img
            src="../assets/Login/loginpage.png"
            alt="user illustration"
            className={scss.image}
          />
        </div>
        <div className="flex items-center max-h-full">
          <Paper
            shadow="none"
            w="100%"
            h="100%"
            px={{ base: "lg", sm: "xl" }}
            py="md"
            pos="relative"
            className="overflow-y-auto"
          >
            <div className="flex flex-col gap-2 h-100">
              <div className={scss.signIn}>
                <h3 className={scss.title}>Sign In</h3>
              </div>
              <form
                noValidate
                onSubmit={handleSubmit(onSubmit)}
                className="flex flex-col gap-3"
              >
                <div className="flex flex-wrap gap-3">
                  <div className="min-w-52 flex-1">
                    <TextInput
                      leftSection={<FaRegUser />}
                      label="Your Name"
                      autoComplete="new"
                      maxLength={20}
                      error={errors.name?.message}
                      {...register("name")}
                    />
                  </div>

                  <div className="min-w-52 flex-1">
                    <TextInput
                      leftSection={<FaRegUser />}
                      label="Mobile No."
                      autoComplete="new"
                      maxLength={20}
                      error={errors.username?.message}
                      {...register("username")}
                    />
                  </div>
                </div>

                <div className="flex flex-wrap gap-3">
                  <div className="min-w-52 flex-1">
                    <TextInput
                      label="Email"
                      error={errors.email?.message}
                      leftSection={<MdOutlineMailOutline />}
                      {...register("email")}
                      type="email"
                    />
                  </div>
                </div>

                <div className="flex flex-wrap gap-3">
                  <div className="min-w-52 flex-1">
                    <PasswordInput
                      label="Password"
                      leftSection={<RiLockPasswordLine />}
                      autoComplete="new-password"
                      error={errors.password?.message}
                      {...register("password")}
                    />
                  </div>
                  <div className="min-w-52 flex-1">
                    <PasswordInput
                      leftSection={<RiLockPasswordLine />}
                      label="Confirm Password"
                      error={errors.confirmPassword?.message}
                      {...register("confirmPassword")}
                    />
                  </div>
                </div>

                <div className="flex flex-col items-center mt-1">
                  <Button
                    className="tracking-wide"
                    color="blue.9"
                    size="md"
                    radius="xl"
                    px="xl"
                    type="submit"
                  >
                    Sign Up
                  </Button>
                </div>
              </form>
              <div className="flex justify-center items-center">
                <p className="my-4 flex gap-2  text-sm">
                  Already have an account?
                  <Link
                    className="text-primary flex gap-1 items-center"
                    href="/login"
                  >
                    Sign In
                    <FiExternalLink size={14} className="mb-1" />
                  </Link>
                </p>
              </div>
            </div>
          </Paper>
        </div>
      </Paper>
    </div>
  );
};

export default RagisterPage;
