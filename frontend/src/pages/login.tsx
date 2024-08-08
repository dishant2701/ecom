// import LoginPage from '@/components/Login'
import dynamic from 'next/dynamic';
import React from 'react'


const LoginPage = dynamic(() => import("../components/Login"), { ssr: false });

const Login = () => {
  return (
   <LoginPage/>
  )
}

export default Login