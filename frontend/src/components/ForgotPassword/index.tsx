import React, { useState } from "react";
import { useRouter } from "next/router";
import { useForm, Controller } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import * as Yup from "yup";
import {
  TextInput,
  PasswordInput,
  Button,
  Box,
  Text,
  PinInput,
  Group,
} from "@mantine/core";

import fetcher from "@/utils/fetcher";
import { toast } from "react-toastify";

interface Props {
  onClose: () => void;
}

enum FormStep {
  USERNAME = 0,
  OTP,
  PASSWORDS,
}

interface UsernameSchema {
  username: string;
  roleId: string;
}

interface OtpSchema {
  forgotOtp: string;
  roleId: string;
}

interface PasswordSchema {
  password: string;
  confirmPassword: string;
  roleId: string;
}

type FormSchema = UsernameSchema & OtpSchema & PasswordSchema;

const usernameSchema: any = Yup.object().shape({
  username: Yup.string()
    .required("Please Enter Mobile Number")
    .matches(/^[0-9]{10}$/, "Mobile number must be 10 digits"),
});

const otpSchema: any = Yup.object().shape({
  forgotOtp: Yup.string()
    .length(6, "Must be exactly 6 characters")
    .required("OTP is required"),
});

const passwordSchema: any = Yup.object().shape({
  password: Yup.string()
    .matches(
      /^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*[0-9])(?=.*[a-z].*[a-z].*[a-z]).{8,64}$/,
      "Length 8 or more including digit, uppercase, lowercase & special characters"
    )
    .required("Password is required"),
  confirmPassword: Yup.string()
    .oneOf([Yup.ref("password")], "Passwords must match")
    .required("Confirm Password is required"),
});

const ForgotPassword: React.FC<Props> = ({ onClose }) => {
  const router = useRouter();
  const [finalResult, setFinalResult] = useState<Partial<FormSchema>>({});
  const [step, setStep] = useState<FormStep>(FormStep.USERNAME);

  const generateOtp = async (data: UsernameSchema | FormSchema) => {
    try {
      console.log("first", data);
      const res = await fetcher("/auth/reset-password/send-OTP", "POST", {
        ...data,
      });

      if (!res || res.error) {
        toast.error(res.error);
        setStep(FormStep.OTP);
        return;
      } else if (typeof res === "string") {
        toast.success(res);
      }
      // console.log("response", res);
      setStep(FormStep.OTP);
      setFinalResult((prev) => ({ ...prev, ...data }));
    } catch (error: any) {
      toast.error(error.message);
    }
  };

  const submitOtp = async (data: OtpSchema | FormSchema) => {
    try {
      // console.log("data", data);

      const res = await fetcher("/auth/reset-password/verify-OTP", "POST", {
        ...finalResult,
        ...data,
      });

      // console.log("verify :", res);
      if (!res || res?.error) {
        toast.error(res.error);
        return;
      }
      setStep(FormStep.PASSWORDS);
      setFinalResult((prev) => ({ ...prev, ...data }));
    } catch (err: any) {
      toast.error(err.message);
    }
  };

  const resetPassword = async (data: PasswordSchema) => {
    const res = await fetcher("/auth/reset-password", "POST", {
      ...finalResult,
      password: data.password,
    });
    if (!res || res?.error) {
      toast.error(res.error);
      return;
    }
    if (typeof res === "string") {
      toast.success(res);
      onClose();
      localStorage.clear();
      sessionStorage.clear();
      window.open("/react/home/", "_self");
    }
  };

  const usernameForm = useForm<UsernameSchema>({
    resolver: yupResolver(usernameSchema),
    defaultValues: {
      roleId: "2",
    },
  });

  const otpForm = useForm<OtpSchema>({
    resolver: yupResolver(otpSchema),
    defaultValues: {
      roleId: "2",
    },
  });
  const passwordForm = useForm<PasswordSchema>({
    resolver: yupResolver(passwordSchema),
    defaultValues: {
      roleId: "2",
    },
  });

  console.log("usernameForm.formState.errors.", usernameForm.formState.errors);
  return (
    <>
      <div className="container-fluid">
        {step === FormStep.USERNAME && (
          <form noValidate onSubmit={usernameForm.handleSubmit(generateOtp)}>
            <Text mb={5}>Enter username</Text>
            <TextInput
              placeholder="Username"
              {...usernameForm.register("username")}
              error={usernameForm.formState.errors.username?.message}
            />
            <Button type="submit" fullWidth mt={20}>
              SUBMIT
            </Button>
          </form>
        )}

        {step === FormStep.OTP && (
          <form noValidate onSubmit={otpForm.handleSubmit(submitOtp)}>
            <Text mb={5} style={{ textAlign: "center" }}>
              Enter the OTP sent to your registered mobile number / email ID
            </Text>
            <Controller
              name="forgotOtp"
              control={otpForm.control}
              render={({ field }) => (
                <Group justify="center">
                  <PinInput autoFocus length={6} {...field} />
                </Group>
              )}
            />
            <Button type="submit" fullWidth mt={20}>
              SUBMIT
            </Button>
            <Box mt={10} style={{ textAlign: "center" }}>
              <Text size="sm">Did not receive yet?</Text>
              <Button
                variant="link"
                onClick={() => generateOtp(finalResult as Required<FormSchema>)}
              >
                Resend OTP
              </Button>
            </Box>
          </form>
        )}

        {step === FormStep.PASSWORDS && (
          <form noValidate onSubmit={passwordForm.handleSubmit(resetPassword)}>
            <Text mb={5}>Enter new password</Text>
            <PasswordInput
              placeholder="New password"
              {...passwordForm.register("password")}
              error={passwordForm.formState.errors.password?.message}
            />
            <PasswordInput
              placeholder="Confirm new password"
              {...passwordForm.register("confirmPassword")}
              error={passwordForm.formState.errors.confirmPassword?.message}
              mt={10}
            />
            <Button type="submit" fullWidth mt={20}>
              SUBMIT
            </Button>
          </form>
        )}
      </div>
    </>
  );
};

export default ForgotPassword;
