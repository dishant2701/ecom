import "@/styles/globals.css";
import type { AppProps } from "next/app";
import "bootstrap/dist/css/bootstrap.min.css";
import "@mantine/core/styles.css";
import { ModalsProvider } from "@mantine/modals";

import { Inter } from "next/font/google";

import { createTheme, MantineProvider } from "@mantine/core";
import Toastify from "@/components/Toastify";

export const inter = Inter({ subsets: ["latin"] });

const theme = createTheme({
  fontFamily: `${inter.style.fontFamily}, sans-serif`,
  headings: { fontFamily: `${inter.style.fontFamily}, sans-serif` },
});

export default function App({ Component, pageProps }: AppProps) {
  return  <MantineProvider theme={theme}>
  <ModalsProvider>
      <Toastify />
      <Component {...pageProps} />
  </ModalsProvider>
</MantineProvider>
}
