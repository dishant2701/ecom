import Switch from "@/components/Switch/Switch";
import { clsx } from "@/utils/string";
import { Title, useMantineColorScheme } from "@mantine/core";
import React from "react";
import { PiTShirtFill } from "react-icons/pi";
import scss from "./homeheader.module.scss";

function HomeHeader() {
  const { colorScheme } = useMantineColorScheme();

  return (
    <>
      <header
        className={clsx(scss.header, "px-3 py-2 ")}
        style={{
          backgroundColor: colorScheme == "light" ? "#F8F9FA" : "#2E2E2E",
        }}
      >
        <Title
          order={1}
          display="flex"
          className="items-center gap-1 tracking-wider font-normal cursor-pointer"
        >
          {<PiTShirtFill size={29} />}
          Ecomm
        </Title>
        <div className="flex items-center ">
          <Switch />
        </div>
      </header>
    </>
  );
}

export default HomeHeader;
