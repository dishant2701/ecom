import React from "react";
import scss from "./header.module.scss";
import { clsx } from "@/utils/string";
import { ROLE, useUserStore } from "@/stores/user-store";
import { FaPowerOff } from "react-icons/fa";
import { CgProfile } from "react-icons/cg";
import {
  Button,
  CloseButton,
  Drawer,
  Menu,
  Title,
  useMantineColorScheme,
} from "@mantine/core";
import { useDisclosure } from "@mantine/hooks";
import { useRouter } from "next/router";
import Switch from "@/components/Switch/Switch";
import { IoIosMenu } from "react-icons/io";
import fetcher, { API_BASE_URL } from "@/utils/fetcher";
import { toast } from "react-toastify";
import { PiTShirtFill } from "react-icons/pi";

function Header() {
  const router = useRouter();
  const { colorScheme } = useMantineColorScheme();
  const user = useUserStore.use.user();

  const [opened, { open, close }] = useDisclosure(false);

  const logout = async () => {
    try {
      const response = await fetcher("/auth/logout", "POST");

      if (response == "Session cleared") {
        window.sessionStorage.clear();
        window.open("/tms/login", "_self");
      }
    } catch (error: any) {
      toast.error(error.message);
    }
  };

  const handleprofile = () => {
    router.push("/profile");
    close();
  };

  return (
    <header>
      <div
        className={clsx(
          scss.header,
          "px-3 py-2 flex justify-between items-center"
        )}
        style={{
          backgroundColor: colorScheme == "light" ? "#F8F9FA" : "#2E2E2E",
        }}
      >
        <Title
          order={1}
          display="flex"
          className="items-center gap-1 tracking-wider font-normal cursor-pointer"
          onClick={() => router.push("/dashboard/home")}
        >
          <PiTShirtFill size={28} />
          Ecomm
        </Title>
        <div className={scss.header_content}>
          <span className={scss.header_switch}>
            <Switch />
          </span>

          <Drawer
            opened={opened}
            onClose={close}
            withCloseButton={false}
            position="right"
            overlayProps={{ backgroundOpacity: 0.5, blur: 4 }}
          >
            <div className="flex items-center justify-between w-100">
              <span className={scss.userrr}>
                {user.username} (
                {user.roles[0].roleId != ROLE.ADMIN && user.employeeId})
              </span>
              <div className="flex items-center gap-1">
                <Switch />
                <CloseButton size="xl" onClick={close} />
              </div>
            </div>
            <div className="flex flex-column my-4 gap-3">
              {user.roles[0].roleId != ROLE.ADMIN && (
                <Button
                  fullWidth
                  variant="default"
                  justify="flex-start"
                  leftSection={<CgProfile className="mr-2" size={24} />}
                  size="xl"
                  onClick={handleprofile}
                >
                  Profile
                </Button>
              )}

              <Button
                fullWidth
                justify="flex-start"
                variant="default"
                leftSection={<FaPowerOff className="mr-2" size={24} />}
                size="xl"
                onClick={logout}
              >
                Logout
              </Button>
            </div>
          </Drawer>

          <span className={scss.menuIcon} onClick={open}>
            <IoIosMenu size={30} />
          </span>

          <div className={scss.Header_Profile}>
            <div className="flex items-center rounded">
              <Menu width="Target" shadow="md" withArrow>
                <Menu.Target>
                  <Button
                    className="flex items-center gap-3"
                    size="compact-lg"
                    variant="subtle"
                    color="blue"
                    h="auto"
                    py={0}
                    fz="md"
                    rightSection={
                      <img
                        style={{
                          height: "2.6rem",
                          width: "2.6rem",
                          borderRadius: "50%",
                          marginLeft: "0.2rem",
                        }}
                        src={
                          user.profilePhoto
                            ? `${API_BASE_URL}/files/avatars/${user.profilePhoto}.png`
                            : user.gender == "male"
                            ? "/tms/assets/avatar/male.svg"
                            : "/tms/assets/avatar/female.svg"
                        }
                        alt="profile"
                        onError={({ currentTarget }) =>
                          (currentTarget.src =
                            user.gender == "male"
                              ? "/tms/assets/avatar/male.svg"
                              : "/tms/assets/avatar/female.svg")
                        }
                      />
                    }
                  >
                    {user.username} (
                    {user.roles[0].roleId != ROLE.ADMIN && user.employeeId})
                  </Button>
                </Menu.Target>

                <Menu.Dropdown>
                  {user.roles[0].roleId != ROLE.ADMIN && (
                    <Menu.Item
                      leftSection={<CgProfile size={20} />}
                      onClick={() => router.push("/profile")}
                    >
                      Profile
                    </Menu.Item>
                  )}

                  <Menu.Item leftSection={<FaPowerOff />} onClick={logout}>
                    Logout
                  </Menu.Item>
                </Menu.Dropdown>
              </Menu>
            </div>
          </div>
        </div>
      </div>
    </header>
  );
}

export default Header;
