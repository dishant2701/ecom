import React from 'react'
import scss from  "./login.module.scss"
import * as Yup from "yup"
import { SubmitHandler, useForm } from 'react-hook-form'
import { yupResolver } from '@hookform/resolvers/yup'
import { Box, Button, LoadingOverlay, Paper, PasswordInput, TextInput } from '@mantine/core'
import { FaRegUser } from 'react-icons/fa'
import { RiLockPasswordLine } from 'react-icons/ri'
import { HiArrowRight } from 'react-icons/hi'
import Link from 'next/link'
import { FiExternalLink } from 'react-icons/fi'

type LoginDate={
    username:string
    password:string
}
const validayionSchema=Yup.object().shape({
    username:Yup.string()
    .required("Please Enter Mobile Number")
    .matches(/^[0-9]{10}$/, "Mobile number must be 10 digits"),
    password:Yup.string().required("Please enter Password")
})
const LoginPage = () => {
    const{
        register,
        handleSubmit,
        formState: { errors },
    }=useForm<LoginDate>({
        resolver:yupResolver(validayionSchema),
        mode:"onChange"
    })

    const onSubmit: SubmitHandler<LoginDate> = async (data) => {
        console.log("data",data)
    }

  return (
    <div className={scss.container}>
         <Paper withBorder shadow="md" p="lg" className={scss.gridWrap}>
         <div className="p-8 flex items-center justify-center max-[856px]:hidden border-r-2 border-slate-200">
          <img
            src="../assets/Login/loginpage.png"
            alt="user illustration"
            className="max-h-full"
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
            className="overflow-y-auto">
          
            <div className="flex flex-col gap-2 h-100">
              <div className={scss.signIn}>
                <h3 className={scss.title}>Sign In</h3>
               
              </div>
              <form
                noValidate
                onSubmit={handleSubmit(onSubmit)}
                className="flex flex-col gap-3">
               
                <div>
                  <TextInput
                    label="Username"
                    leftSection={<FaRegUser />}
                    {...register("username")}
                    error={errors.username?.message}
                  />
                </div>
                <div>
                  <PasswordInput
                    label="Password"
                    leftSection={<RiLockPasswordLine />}
                    {...register("password")}
                    error={errors.password?.message}
                  />
                </div>

                <div className="d-flex justify-content-center mt-3 align-items-center">
                  <Button
                    className="tracking-wide"
                    rightSection={<HiArrowRight size={18} />}
                    color="blue.9"
                    size="md"
                    radius="xl"
                    px="xl"
                    type="submit">
                     Sign In
                  </Button>
                </div>

                <div className="flex justify-center items-center">
                  <p className="mt-2 flex gap-2">
                    Don't have an account?
                    <Link
                      className="text-primary flex gap-1 items-center"
                      href="/register">
                      Sign Up
                      <FiExternalLink size={14} className="mb-1" />
                    </Link>
                  </p>
                </div>
              </form>
            </div>
          </Paper>
        </div>
         </Paper>
    </div>
  )
}

export default LoginPage